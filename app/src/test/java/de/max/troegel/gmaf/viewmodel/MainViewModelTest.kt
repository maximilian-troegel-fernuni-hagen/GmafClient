package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.max.troegel.gmaf.data.repository.QueryRepository
import de.max.troegel.gmaf.data.repository.SettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertEquals(QueryType.FIND_SIMILAR_MEDIA, query?.getQueryType())
        assertEquals("a giant cat", query?.getQueryText())
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
        assertEquals(QueryType.FIND_RECOMMENDED_MEDIA, query?.getQueryType())
        assertEquals("a small elephant", query?.getQueryText())
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

    private var expectedWords = 0
    private var identifiedWords = 0
    private var expectedWordMap = mutableMapOf<Int, Int>()
    private var identifiedWordMap = mutableMapOf<Int, Int>()
    private var mainViewModel: MainViewModel? = null
    private val testPhrases = mutableListOf(
        "Golden Retriever puppy",
        "Beautiful sunset over the ocean",
        "Close-up of a flower",
        "New York City skyline",
        "Plate of sushi rolls",
        "Snowy mountain peak",
        "Colorful street art graffiti",
        "Vintage car on a city street",
        "Group of friends at a beach bonfire",
        "Famous landmark in Paris",
        "Rustic farmhouse kitchen",
        "Hiking trail in the mountains",
        "Abstract art painting",
        "Vintage record player",
        "Luxury sports car interior",
        "Close-up of a butterfly",
        "Snowy winter landscape",
        "Modern office workspace",
        "Family playing board games",
        "Famous painting by Van Gogh",
        "Vintage camera collection",
        "Futuristic cityscape",
        "Colorful hot air balloons",
        "Exotic beach destination",
        "Garden with blooming flowers",
        "Majestic waterfall landscape",
        "Artificial intelligence concept",
        "Classic book covers",
        "Family playing in the park",
        "Historical landmarks in Rome",
        "Colorful autumn leaves",
        "City street food vendors",
        "Retro fashion trends",
        "Professional athletes in action",
        "Rooftop bar with a view",
        "Abstract geometric patterns",
        "Beautiful wedding ceremony",
        "Aerial view of a tropical island",
        "Vintage travel posters",
        "Artistic street murals",
        "Luxury cruise ship interior",
        "Abstract watercolor painting",
        "Tropical bird species",
        "Amusement park roller coasters",
        "Yoga poses on the beach",
        "Street performers in New Orleans",
        "Vintage travel trailers",
        "Bridges of the world",
        "Natural rock formations",
        "Gothic architecture details",
        "Christmas holiday decorations",
        "Beautiful Italian gardens",
        "Rural farmland landscape",
        "Fashion runway models",
        "Canyon hiking trail",
        "Luxury hotel pool and spa",
        "Historical artifacts and antiques",
        "Delicious French pastries",
        "Street fashion in Tokyo",
        "Famous movie scenes",
        "Street art murals in Berlin",
        "Vintage bicycles and motorcycles",
        "Urban city graffiti",
        "Beautiful sunflowers in a field",
        "Fashion accessories on display",
        "Retro movie posters",
        "Famous bridges of San Francisco",
        "Cherry blossom trees in bloom",
        "Professional dancers in motion",
        "Street food markets in Asia",
        "Exotic fish species",
        "Ancient Egyptian hieroglyphics",
        "Beautiful coral reefs",
        "Scenic mountain roads",
        "Extravagant wedding receptions",
        "Artistic pottery designs",
        "Tropical island paradise",
        "Vintage vinyl record collections",
        "Artistic black and white photography",
        "Traditional tribal patterns",
        "Graffiti street art in New York",
        "Famous monuments in Washington D.C.",
        "Breathtaking night sky photography",
        "Beautiful garden fountains",
        "African wildlife photography",
        "Famous book covers",
        "Urban street photography",
        "Vintage pin-up girls",
        "Luxury home interiors",
        "Famous abstract paintings",
        "Colorful street art in Rio de Janeiro",
        "Beautiful night views of Hong Kong",
        "Fashionable street style in Paris",
        "Scenic hiking trails in the Swiss Alps",
        "Graffiti art in Melbourne's laneways",
        "Historic landmarks in London",
        "Exotic fruits from around the world",
        "Stunning sunset views over the ocean",
        "Modern architecture in Dubai",
        "Spectacular views from hot air balloons",
        "Vintage cars in Cuba",
        "African safari animals",
        "Beautiful waterfalls around the world",
        "Street performers in Barcelona",
        "Ancient Greek sculptures",
        "Famous movie posters",
        "Exotic birds of paradise",
        "Luxury beach resorts",
        "Famous paintings of Vincent Van Gogh",
        "Urban graffiti art in London",
        "Spectacular views of Niagara Falls",
        "Delicious sushi platters",
        "High-end luxury watches",
        "Famous landmarks in Paris",
        "Abstract oil paintings",
        "Tropical rainforest wildlife",
        "Elegant wedding dresses",
        "Rustic farmhouses in Tuscany",
        "Colorful hot air balloons",
        "Street fashion in New York",
        "Exotic butterfly species",
        "Beautiful flower gardens",
        "Historic castles of Europe",
        "Professional chefs cooking in a restaurant",
        "Artistic street performers in Amsterdam",
        "Classic Hollywood movie stars",
        "Famous modern art installations",
        "Beautiful lakeside cabins",
        "Japanese cherry blossom trees",
        "Famous landmarks in New York City",
        "Abstract modern sculptures",
        "Colorful street markets in Morocco",
        "Tropical island beaches",
        "Traditional Japanese pottery",
        "Vintage fashion ads",
        "Spectacular views of the Grand Canyon",
        "Exotic sea creatures",
        "Artistic murals in Mexico City",
        "Retro arcade games",
        "Gothic cathedral interiors",
        "Historical landmarks in Rome",
        "Beautiful underwater coral reefs",
        "Exotic flowers from around the world",
        "Graffiti art in Berlin",
        "Fashionable street style in Tokyo",
        "Artistic black and white portraits",
        "Famous landmarks in San Francisco",
        "Colorful street festivals around the world",
        "Modern architecture in New York",
        "Elegant ballroom dance performances",
        "Famous sculptures of Michelangelo",
        "Luxury penthouse apartments",
        "Tropical island sunsets",
        "Vintage camera collections",
        "Artistic graffiti murals in Los Angeles",
        "Historic buildings in Prague",
        "Spectacular views of the Swiss Alps",
        "Famous art pieces of Salvador Dali",
        "Exotic spices and herbs",
        "Beautiful autumn landscapes",
        "Abstract geometric sculptures",
        "Colorful Indian street food",
        "Luxury yachts in the Mediterranean",
        "Famous landmarks in Tokyo",
        "Ancient Egyptian pyramids",
        "Artistic abstract photography",
        "Delicious Italian cuisine",
        "Historic landmarks in Istanbul",
        "Exotic marine life",
        "Artistic murals in Rio de Janeiro",
        "Retro rock band posters",
        "Famous sculptures of Rodin",
        "Urban street art in Melbourne",
        "Spectacular views of Mount Everest",
        "Exotic cactus species",
        "Elegant ballgowns and tuxedos",
        "Vintage record players",
        "Historic landmarks in Beijing",
        "Beautiful coastal views",
        "Abstract modern paintings",
        "Tropical rainforest waterfalls",
        "Vintage posters from the golden age of travel",
        "Famous art pieces of Pablo Picasso",
        "Modern urban architecture in Singapore",
        "Spectacular views of the Northern Lights",
        "Exotic orchid species",
        "Colorful graffiti art in Sao Paulo",
        "Elegant high-end jewelry",
        "Historic landmarks in Athens",
        "Beautiful mountain vistas",
        "Abstract contemporary paintings",
        "Tropical island water sports",
        "Artistic tattoos on skin",
        "Famous street art murals in Paris",
        "Retro vinyl album covers",
        "Historic landmarks in Kyoto",
        "Exotic marine creatures in the Great Barrier Reef",
        "Luxury sports cars",
        "Traditional Chinese calligraphy",
        "Vintage pin-up girl posters"
    )

    @Test
    fun evaluateIntentProcessing() {
        val queryRepository: QueryRepository = mock(QueryRepository::class.java)
        val settingsRepository: SettingsRepository = mock(SettingsRepository::class.java)
        mainViewModel = MainViewModel(queryRepository, settingsRepository)
        val data = mock(Uri::class.java)
        `when`(data.encodedPath).thenReturn("/search_recommended_media/")
        val intent = mock(Intent::class.java)
        `when`(intent.action).thenReturn(Intent.ACTION_VIEW)
        `when`(intent.data).thenReturn(data)
        val captor = ArgumentCaptor.forClass(GmafQuery::class.java)
        for (phrase in testPhrases) {
            evaluatePhrase(phrase, data, intent, captor, queryRepository)
        }
        assertTrue(identifiedWords > 0.99 * expectedWords) // 795 from 803
    }

    private fun evaluatePhrase(
        phrase: String, data: Uri, intent: Intent,
        captor: ArgumentCaptor<GmafQuery>,
        queryRepository: QueryRepository
    ) {
        `when`(data.getQueryParameter("media_description")).thenReturn(phrase)
        mainViewModel?.handleIntent(intent, mock(Context::class.java))
        verify(queryRepository, atLeastOnce()).setQuery(captor.capture())
        val query = captor.value
        assertEquals(QueryType.FIND_RECOMMENDED_MEDIA, query?.getQueryType())
        assertEquals(phrase, query?.getQueryText())
        val phraseWords = phrase.lowercase().split(" ")
        var identifiedWordCount = phraseWords.size
        for (word in phraseWords) {
            expectedWords++
            if (query.getGraphCode().dictionary.contains(word)) {
                identifiedWords++
            } else {
                identifiedWordCount--
            }
            expectedWordMap[expectedWordMap.size] = phraseWords.size
            identifiedWordMap[identifiedWordMap.size] = identifiedWordCount
        }
    }
}