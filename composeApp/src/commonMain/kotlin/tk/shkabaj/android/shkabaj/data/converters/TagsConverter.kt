package tk.shkabaj.android.shkabaj.data.converters

import androidx.room.TypeConverter

class TagsConverter {

    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        return tags.joinToString(",")
    }

    @TypeConverter
    fun toTagsList(tags: String): List<String> {
        return tags.split(",")
    }
}