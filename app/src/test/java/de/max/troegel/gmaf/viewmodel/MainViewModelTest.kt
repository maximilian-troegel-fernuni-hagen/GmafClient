package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(
    RobolectricTestRunner::class
)
class MainViewModelTest {

    @Test
    fun handleIntent_similarMedia() {
        val queryRepository = mock(QueryRepository::class.java)
        val settingsRepository = mock(SettingsRepository::class.java)
        val mainViewModel = MainViewModel(queryRepository, settingsRepository)
        val data = mock(Uri::class.java)
        `when`(data.encodedPath).thenReturn("/search_similar_media/")
        `when`(data.getQueryParameter("media_description")).thenReturn("a giant cat")
        val intent = mock(Intent::class.java)
        `when`(intent.action).thenReturn(Intent.ACTION_VIEW)
        `when`(intent.data).thenReturn(data)
        mainViewModel.handleIntent(intent, mock(Context::class.java))
        val captor = ArgumentCaptor.forClass(GmafQuery::class.java)
        verify(queryRepository).setQuery(captor.capture())
        val query = captor.value
        Assert.assertEquals(QueryType.FIND_SIMILAR_MEDIA, query?.getQueryType())
        Assert.assertEquals("a giant cat", query?.getQueryText())
    }

    @Test
    fun handleIntent_recommendedMedia() {
        val queryRepository = mock(QueryRepository::class.java)
        val settingsRepository = mock(SettingsRepository::class.java)
        val mainViewModel = MainViewModel(queryRepository, settingsRepository)
        val data = mock(Uri::class.java)
        `when`(data.encodedPath).thenReturn("/search_recommended_media/")
        `when`(data.getQueryParameter("media_description")).thenReturn("a small elephant")
        val intent = mock(Intent::class.java)
        `when`(intent.action).thenReturn(Intent.ACTION_VIEW)
        `when`(intent.data).thenReturn(data)
        mainViewModel.handleIntent(intent, mock(Context::class.java))
        val captor = ArgumentCaptor.forClass(GmafQuery::class.java)
        verify(queryRepository).setQuery(captor.capture())
        val query = captor.value
        Assert.assertEquals(QueryType.FIND_RECOMMENDED_MEDIA, query?.getQueryType())
        Assert.assertEquals("a small elephant", query?.getQueryText())
    }

    @Test(expected = NullPointerException::class)
    fun handleIntent_invalidAction() {
        val queryRepository = mock(QueryRepository::class.java)
        val settingsRepository = mock(SettingsRepository::class.java)
        val mainViewModel = MainViewModel(queryRepository, settingsRepository)
        val intent = mock(Intent::class.java)
        val context = mock(Context::class.java)
        `when`(intent.action).thenReturn(Intent.ACTION_ANSWER)
        mainViewModel.handleIntent(intent, context)
        val captor = ArgumentCaptor.forClass(GmafQuery::class.java)
        verify(queryRepository, never()).setQuery(captor.capture())
        verify(context).getString(anyInt())
    }
}