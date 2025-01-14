package tk.shkabaj.android.shkabaj.ui.news.model

enum class NewsFilterModel {
    DEFAULT,
    DATE_FILTER;

    val title: String
        get() = when(this) {
            DEFAULT -> "Default filter"
            DATE_FILTER -> "According to the latest news"
        }
}