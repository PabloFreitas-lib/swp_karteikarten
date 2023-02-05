package uni.myosotis.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.persistence.IndexcardRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.joor.Reflect.on;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EditIndexcardTest {
    private IndexcardRepository indexcardRepMock;
    private IndexcardLogic indexcardLogic;

    @BeforeEach
    public void beforeEach() {
        indexcardRepMock = mock(IndexcardRepository.class);
        indexcardLogic = new IndexcardLogic();
        on(indexcardLogic).set("indexcardRepository", indexcardRepMock);
    }

    @Test
    public void testExceptionIfIndexcardNotExists() {
        final String expected = "Die zu bearbeitende Karteikarte existiert nicht.";
        when(indexcardRepMock.getIndexcardById(1L)).thenReturn(Optional.empty());
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> indexcardLogic.updateIndexcard("name", "question", "answer", new ArrayList<>(), new ArrayList<>(), 1L));
        assertEquals(expected, exception.getMessage());
        verify(indexcardRepMock).getIndexcardById(1L);
    }

    @Test
    public void testExceptionIfDatabaseError() {
        final Indexcard mockIndexcard = mock(Indexcard.class);
        final String expected = "Die Karteikarte konnte nicht aktualisiert werden.";
        when(mockIndexcard.getId()).thenReturn(1L);
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.updateIndexcard(mockIndexcard)).thenReturn(-1);
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> indexcardLogic.updateIndexcard("name", "question", "answer", new ArrayList<>(), new ArrayList<>(), 1L));
        assertEquals(expected, exception.getMessage());
        verify(mockIndexcard).getId();
        verify(indexcardRepMock).updateIndexcard(mockIndexcard);
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
    }

    @Test
    public void testNoExceptionsIfIndexcardUpdatedCorrectly() {
        final Indexcard mockIndexcard = mock(Indexcard.class);
        when(mockIndexcard.getId()).thenReturn(1L);
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.updateIndexcard(mockIndexcard)).thenReturn(0);
        assertDoesNotThrow(() -> indexcardLogic.updateIndexcard("newName", "newQuestion", "newAnswer", new ArrayList<>(), new ArrayList<>(), mockIndexcard.getId()));
        verify(mockIndexcard, times(2)).getId();
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
        verify(indexcardRepMock).updateIndexcard(mockIndexcard);
    }
}
