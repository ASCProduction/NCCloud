package tk.shkabaj.android.shkabaj.ui.news.model

enum class NewsFilterModel {
    DEFAULT,
    DATE_FILTER;

    val title: String
        get() = when(this) {
            DEFAULT -> "E parazgjedhur"
            DATE_FILTER -> "Sipas perditesimit te fundit"
        }
}