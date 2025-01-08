package tk.shkabaj.android.shkabaj.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nccloud.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.CryptoTitle
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.theme.CulturedGreyColor

@Composable
fun CryptoCard(
    cryptoList: List<CryptoInfo>,
    onCryptoClick: (cryptoItem: CryptoInfo, isLong: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.background(CulturedGreyColor)
    ) {
        CardHeader(
            title = stringResource(Res.string.CryptoTitle),
            showMore = false
        )
        CryptoHorizontalList(
            cryptoList = cryptoList,
            onCryptoClick = onCryptoClick
        )
    }
}