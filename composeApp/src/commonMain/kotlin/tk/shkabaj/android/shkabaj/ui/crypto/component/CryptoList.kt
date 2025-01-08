package tk.shkabaj.android.shkabaj.ui.crypto.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tk.shkabaj.android.shkabaj.network.entity.crypto.CryptoInfo
import tk.shkabaj.android.shkabaj.ui.theme.MainBGColor

@Composable
fun CryptoList(
    cryptoItem: List<CryptoInfo>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        CryptoItem(
            cryptoItem = cryptoItem[0],
            modifier = Modifier
                .height(235.dp)
                .background(MainBGColor, RoundedCornerShape(4.dp))
                .padding(0.dp)
                .weight(1f)
        )
        if(cryptoItem.size > 1) {
            CryptoItem(
                cryptoItem = cryptoItem[1],
                modifier = Modifier
                    .height(235.dp)
                    .background(MainBGColor, RoundedCornerShape(4.dp))
                    .padding(0.dp)
                    .weight(1f)
            )
        } else {
            Spacer(
                modifier = Modifier.weight(1f)
            )
        }
    }
}