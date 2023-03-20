package de.max.troegel.gmaf.viewmodel

import android.content.Context
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.data.model.QueryType
import de.swa.gc.GraphCode
import gurtek.com.offlinedictionary.Dictionary
import okhttp3.internal.immutableListOf
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

@RunWith(
    RobolectricTestRunner::class
)
class DetailsViewModelTest {
    private val colors: List<Int> = immutableListOf(
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW,
        Color.MAGENTA,
        Color.GRAY
    )

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

    @Test
    fun calculate() {
        Dictionary.init(ApplicationProvider.getApplicationContext())
        Dictionary.getEnglishDictionary().importSdDatabase("src/test/resources/databases/").subscribe()
        val context: Context = ApplicationProvider.getApplicationContext()
        shadowOf(context.mainLooper).idle()
        Thread.sleep(2000)
        val contextt: Context = ApplicationProvider.getApplicationContext()
        shadowOf(contextt.mainLooper).idle()
        val detailsViewModel = DetailsViewModel(colors)
        val graphCodes = mutableListOf<GraphCode>()
        val gson = Gson()
        for (i in 1..16) {
            val fileName = "$i.json"
            val stream = this.javaClass.classLoader?.getResourceAsStream(fileName)
            if (stream != null) {
                val reader = BufferedReader(InputStreamReader(stream))
                val gc = gson.fromJson(reader, GraphCode::class.java)
                if (gc != null) {
                    graphCodes.add(gc)
                }
            }
        }
        val testPhrases = mutableListOf(
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
        for (phrase in testPhrases) {
            val query = GmafQuery(Date().time, QueryType.FIND_RECOMMENDED_MEDIA, phrase, generateGraphCodeFor(phrase))
            for (gc in graphCodes) {
                val queryWordOccurrence = detailsViewModel.calculateOccurrence(query, gc)
                for (key in queryWordOccurrence.keys) {
                    println("Word: $key, Value: ${queryWordOccurrence[key]}")
                }
            }
        }
    }

    private fun generateGraphCodeFor(inputText: String): GraphCode {

        val queryComponents: Array<String> = inputText.split(",", " ").toTypedArray()
        val dictionary = Vector<String>()
        for (queryComponent in queryComponents) {
            if (queryComponent.isNotEmpty()) {
                if (!dictionary.contains(queryComponent)) {
                    dictionary.add(queryComponent.trim { it <= ' ' })
                }
            }
        }
        val gcQuery = GraphCode()
        gcQuery.dictionary = Vector(dictionary)
        return gcQuery
    }
}