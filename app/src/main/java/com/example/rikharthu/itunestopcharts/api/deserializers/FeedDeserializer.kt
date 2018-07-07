package com.example.rikharthu.itunestopcharts.api.deserializers

import android.support.annotation.VisibleForTesting
import com.example.rikharthu.itunestopcharts.data.api.models.Entry
import com.example.rikharthu.itunestopcharts.data.api.models.Feed
import com.example.rikharthu.itunestopcharts.data.api.models.Image
import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type

class FeedDeserializer : JsonDeserializer<Feed> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Feed {
        return parseFeed(json)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun parseFeed(feedJson: JsonElement): Feed {
        val entriesJson = feedJson.asJsonObject.get("feed").asJsonObject.get("entry").asJsonArray
        val entries = mutableListOf<Entry>()
        entriesJson.forEach {
            entries.add(parseEntry(it))
        }
        return Feed(entries)
    }

    private fun parseEntry(entryJson: JsonElement): Entry {
        val entryJsonObj = entryJson.asJsonObject
        val entryName = entryJsonObj.get("im:name").getLabel()!!

        val images = mutableListOf<Image>()
        val imagesArray = entryJsonObj.getAsJsonArray("im:image")

        imagesArray.forEach {
            images.add(parseImage(it))
        }

        val price = entryJsonObj.get("im:price").getLabel()!!
        val rights = entryJsonObj.get("rights").getLabel()!!
        val title = entryJsonObj.get("title").getLabel()!!

        val linksArray = entryJsonObj.getAsJsonArray("link")
        var previewLink = ""
        for (linkElement in linksArray) {
            val linkObj = linkElement.asJsonObject
            val linkAttributes = linkObj.getAsJsonObject("attributes")
            val type = linkAttributes.get("type").asString
            if (type == "audio/x-m4a") {
                previewLink = linkAttributes.get("href").asString
                break
            }
        }

        val id = entryJsonObj.getAsJsonObject("id")
                .getAsJsonObject("attributes")
                .get("im:id").asLong

        val artist = entryJsonObj.get("im:artist").getLabel("")!!
        val category = entryJsonObj.getAsJsonObject("category")
                .getAsJsonObject("attributes")
                .getLabel("")!!
        val releaseDate = entryJsonObj.get("im:releaseDate").getLabel()!!

        val album = entryJsonObj.getAsJsonObject("im:collection")
                .get("im:name").getLabel()

        // val largestImage = images.maxBy { it.height }
        return Entry(id.toString(), entryName, artist, title, album!!, previewLink, false)
    }

    private fun parseImage(imageElemet: JsonElement): Image {
        val imageObj = imageElemet.asJsonObject
        val url = imageObj.getLabel()!!
        val height = imageObj.getAsJsonObject("attributes").get("height").asInt
        return Image(url, height)
    }

    fun JsonElement.getLabel(default: String? = null): String? {
        try {
            if (this is JsonObject) {
                val labelElement = this.get("label")
                if (labelElement is JsonPrimitive && labelElement.isString) {
                    return labelElement.asString
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Could not extract label from JsonElement")
        }
        return default
    }
}