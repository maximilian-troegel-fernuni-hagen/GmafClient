package de.max.troegel.gmaf.viewmodel

import android.graphics.Color
import okhttp3.internal.immutableListOf
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(
    RobolectricTestRunner::class)
class DetailsViewModelTest {
    private val colors: List<Int> = immutableListOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW,
        Color.MAGENTA,
        Color.GRAY)

    @Test
    fun getColorForWord() {
        val detailsViewModel = DetailsViewModel(colors)
        Assert.assertEquals(colors[0], detailsViewModel.getColorForWord("First", true, Color.BLACK, 1.0F))
        Assert.assertEquals(colors[1], detailsViewModel.getColorForWord("Second", true, Color.BLACK, 1.0F))
        Assert.assertEquals(colors[0], detailsViewModel.getColorForWord("First", true, Color.BLACK, 1.0F))
        Assert.assertEquals(colors[2], detailsViewModel.getColorForWord("Third", true, Color.BLACK, 1.0F))
    }

    @Test
    fun clearColorMap() {
        val detailsViewModel = DetailsViewModel(colors)
        Assert.assertEquals(colors[0], detailsViewModel.getColorForWord("First", true, Color.BLACK, 1.0F))
        Assert.assertEquals(colors[1], detailsViewModel.getColorForWord("Second", true, Color.BLACK, 1.0F))
        detailsViewModel.clearColorMap()
        Assert.assertEquals(colors[0], detailsViewModel.getColorForWord("Second", true, Color.BLACK, 1.0F))
        Assert.assertEquals(colors[1], detailsViewModel.getColorForWord("First", true, Color.BLACK, 1.0F))
    }
}