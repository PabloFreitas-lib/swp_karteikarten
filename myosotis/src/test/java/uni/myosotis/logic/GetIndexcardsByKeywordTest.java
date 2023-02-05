package uni.myosotis.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.Keyword;
import uni.myosotis.persistence.IndexcardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.joor.Reflect.on;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetIndexcardsByKeywordTest {
    IndexcardLogic indexcardLogic;
    IndexcardRepository indexcardRepMock;

    @BeforeEach
    public void beforeEach() {
        indexcardLogic = new IndexcardLogic();
        indexcardRepMock = mock(IndexcardRepository.class);
        on(indexcardLogic).set("indexcardRepository", indexcardRepMock);
    }

    @Test
    public void testNoIndexcardsIfKeywordNull() {
        final Indexcard mockIndexcard1 = mock(Indexcard.class);
        final Indexcard mockIndexcard2 = mock(Indexcard.class);
        final Keyword mockKeyword = mock(Keyword.class);
        when(indexcardRepMock.getAllIndexcards()).thenReturn(new ArrayList<>(List.of(mockIndexcard1, mockIndexcard2)));
        when(mockIndexcard1.getKeywords()).thenReturn(List.of(mockKeyword));
        when(mockIndexcard2.getKeywords()).thenReturn(List.of());
        when(mockKeyword.getName()).thenReturn("keywordName");
        assertEquals(new ArrayList<>(), indexcardLogic.getIndexcardsByKeyword(null));
        verify(indexcardRepMock).getAllIndexcards();
        verify(mockIndexcard1).getKeywords();
        verify(mockIndexcard2).getKeywords();
        verify(mockKeyword).getName();
    }

    @Test
    public void testReturnOnlyIndexcardsWithTheKeyword() {
        final Indexcard mockIndexcard1 = mock(Indexcard.class);
        final Indexcard mockIndexcard2 = mock(Indexcard.class);
        final Keyword mockKeyword = mock(Keyword.class);
        when(indexcardRepMock.getAllIndexcards()).thenReturn(new ArrayList<>(List.of(mockIndexcard1, mockIndexcard2)));
        when(mockIndexcard1.getKeywords()).thenReturn(List.of(mockKeyword));
        when(mockIndexcard2.getKeywords()).thenReturn(List.of());
        when(mockKeyword.getName()).thenReturn("keywordName");
        assertEquals(new ArrayList<>(List.of(mockIndexcard1)), indexcardLogic.getIndexcardsByKeyword("keywordName"));
        verify(indexcardRepMock).getAllIndexcards();
        verify(mockIndexcard1).getKeywords();
        verify(mockIndexcard2).getKeywords();
        verify(mockKeyword).getName();
    }

    @Test
    public void testReturnAllIndexcardIfAllContainKeyword() {
        final Indexcard mockIndexcard1 = mock(Indexcard.class);
        final Indexcard mockIndexcard2 = mock(Indexcard.class);
        final Keyword mockKeyword = mock(Keyword.class);
        when(indexcardRepMock.getAllIndexcards()).thenReturn(new ArrayList<>(List.of(mockIndexcard1, mockIndexcard2)));
        when(mockIndexcard1.getKeywords()).thenReturn(List.of(mockKeyword));
        when(mockIndexcard2.getKeywords()).thenReturn(List.of(mockKeyword));
        when(mockKeyword.getName()).thenReturn("keywordName");
        assertEquals(new ArrayList<>(List.of(mockIndexcard1, mockIndexcard2)), indexcardLogic.getIndexcardsByKeyword("keywordName"));
        verify(indexcardRepMock).getAllIndexcards();
        verify(mockIndexcard1).getKeywords();
        verify(mockIndexcard2).getKeywords();
        verify(mockKeyword, times(2)).getName();
    }

    @Test
    public void testEmptyIfNoIndexcardsExists() {
        final Keyword mockKeyword = mock(Keyword.class);
        when(indexcardRepMock.getAllIndexcards()).thenReturn(new ArrayList<>());
        when(mockKeyword.getName()).thenReturn("keywordName");
        assertEquals(new ArrayList<>(), indexcardLogic.getIndexcardsByKeyword("keywordName"));
        verify(indexcardRepMock).getAllIndexcards();
    }
}
