package uni.myosotis.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.myosotis.objects.Category;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.persistence.CategoryRepository;
import uni.myosotis.persistence.IndexcardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.joor.Reflect.on;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteIndexcardTest {
    private IndexcardRepository indexcardRepMock;
    private CategoryRepository categoryRepMock;
    private IndexcardLogic indexcardLogic;

    @BeforeEach
    public void beforeEach() {
        indexcardRepMock = mock(IndexcardRepository.class);
        categoryRepMock = mock(CategoryRepository.class);
        indexcardLogic = new IndexcardLogic();
        on(indexcardLogic).set("indexcardRepository", indexcardRepMock);
        on(indexcardLogic).set("categoryRepository", categoryRepMock);
    }

    @Test
    public void testExceptionIfIndexcardNotExists() {
        final String expected = "Die zu löschende Karteikarte existiert nicht.";
        when(indexcardRepMock.getIndexcardById(1L)).thenReturn(Optional.empty());
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> indexcardLogic.deleteIndexcard( 1L));
        assertEquals(expected, exception.getMessage());
        verify(indexcardRepMock).getIndexcardById(1L);
    }

    @Test
    public void testExceptionIfDatabaseError() {
        final Indexcard mockIndexcard = mock(Indexcard.class);
        when(mockIndexcard.getId()).thenReturn(1L);
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.deleteIndexcard(mockIndexcard.getId())).thenReturn(-1);
        when(categoryRepMock.getAllCategories()).thenReturn(new ArrayList<>());
        final String expected = "Die Karteikarte konnte nicht gelöscht werden.";
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> indexcardLogic.deleteIndexcard(mockIndexcard.getId()));
        assertEquals(expected, exception.getMessage());
        verify(mockIndexcard, times(3)).getId();
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
        verify(indexcardRepMock).deleteIndexcard(mockIndexcard.getId());
        verify(categoryRepMock).getAllCategories();
    }

    @Test
    public void testNoErrorsIfIndexcardDeletedCorrectly() {
        final Indexcard mockIndexcard = mock(Indexcard.class);
        when(mockIndexcard.getId()).thenReturn(1L);
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.deleteIndexcard(mockIndexcard.getId())).thenReturn(0);
        when(categoryRepMock.getAllCategories()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> indexcardLogic.deleteIndexcard(mockIndexcard.getId()));
        verify(mockIndexcard, times(3)).getId();
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
        verify(indexcardRepMock).deleteIndexcard(mockIndexcard.getId());
        verify(categoryRepMock).getAllCategories();
    }

    @Test
    public void testCategoriesGetsNotUpdatedIfNotContainsIndexcard() {
        final Category mockCategory = mock(Category.class);
        final Indexcard mockIndexcard = mock(Indexcard.class);
        when(mockIndexcard.getId()).thenReturn(1L);
        when(mockIndexcard.getName()).thenReturn("name");
        when(mockCategory.getIndexcards()).thenReturn(new ArrayList<>());
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.deleteIndexcard(mockIndexcard.getId())).thenReturn(0);
        when(categoryRepMock.getAllCategories()).thenReturn(List.of(mockCategory));
        assertDoesNotThrow(() -> indexcardLogic.deleteIndexcard(mockIndexcard.getId()));
        verify(mockIndexcard, times(3)).getId();
        verify(mockIndexcard, times(1)).getName();
        verify(mockCategory).getIndexcards();
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
        verify(indexcardRepMock).deleteIndexcard(mockIndexcard.getId());
        verify(categoryRepMock).getAllCategories();
    }

    @Test
    public void testCategoriesGetsUpdatedIfContainsIndexcard() {
        final Category mockCategory = mock(Category.class);
        final Indexcard mockIndexcard = mock(Indexcard.class);
        final Indexcard mockIndexcard2 = mock(Indexcard.class);
        final Indexcard mockIndexcard3 = mock(Indexcard.class);
        when(mockIndexcard.getId()).thenReturn(1L);
        when(mockIndexcard.getName()).thenReturn("name");
        when(mockIndexcard2.getId()).thenReturn(2L);
        when(mockIndexcard2.getName()).thenReturn("secondName");
        when(mockIndexcard3.getId()).thenReturn(3L);
        when(mockIndexcard3.getName()).thenReturn("thirdName");
        when(mockCategory.getIndexcards()).thenReturn(List.of(mockIndexcard, mockIndexcard2, mockIndexcard3));
        when(indexcardRepMock.getIndexcardById(mockIndexcard.getId())).thenReturn(Optional.of(mockIndexcard));
        when(indexcardRepMock.deleteIndexcard(mockIndexcard.getId())).thenReturn(0);
        when(indexcardRepMock.getIndexcardByName("secondName")).thenReturn(Optional.of(mockIndexcard2));
        when(indexcardRepMock.getIndexcardByName("thirdName")).thenReturn(Optional.empty());
        when(categoryRepMock.getAllCategories()).thenReturn(List.of(mockCategory));
        assertDoesNotThrow(() -> indexcardLogic.deleteIndexcard(mockIndexcard.getId()));
        verify(mockIndexcard, times(3)).getId();
        verify(mockIndexcard, times(5)).getName();
        verify(mockCategory).getIndexcards();
        verify(indexcardRepMock, times(2)).getIndexcardById(mockIndexcard.getId());
        verify(indexcardRepMock).deleteIndexcard(mockIndexcard.getId());
        verify(categoryRepMock).getAllCategories();
    }
}
