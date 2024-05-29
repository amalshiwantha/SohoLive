package network.common

sealed class UIComponent {

    data class Toast(
        val title: String,
    ) : UIComponent()

    data class Dialog(
        val title: String, val description: String
    ) : UIComponent()
}