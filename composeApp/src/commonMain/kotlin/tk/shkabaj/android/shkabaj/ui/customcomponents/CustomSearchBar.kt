package tk.shkabaj.android.shkabaj.ui.customcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import tk.shkabaj.android.shkabaj.ui.theme.Typography
import org.jetbrains.compose.resources.stringResource
import nccloud.composeapp.generated.resources.CancelSearch
import nccloud.composeapp.generated.resources.ic_clear_text
import nccloud.composeapp.generated.resources.Res
import tk.shkabaj.android.shkabaj.ui.theme.AccentColor
import tk.shkabaj.android.shkabaj.ui.theme.MaastrichtBlueColor
import tk.shkabaj.android.shkabaj.ui.theme.OldSilverColor
import tk.shkabaj.android.shkabaj.ui.theme.SearchBarWhiteColor
import tk.shkabaj.android.shkabaj.ui.theme.SettingsIconColor
import tk.shkabaj.android.shkabaj.utils.Constants

@Composable
fun CustomSearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onFocusedChanged: (Boolean) -> Unit = {},
    placeHolder: String
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    if(text.isEmpty() && isFocused) {
        onFocusedChanged.invoke(true)
    } else {
        onFocusedChanged.invoke(false)
    }

    Box(
        modifier = Modifier
            .height(height = Constants.SEARCHBAR_HEIGHT.dp)
            .background(Color.Transparent)
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().align(alignment = Alignment.Center)) {
            ElevatedCard(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).height(height = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .height(height = 40.dp)
                        .fillMaxWidth()
                        .background(SearchBarWhiteColor, shape = RoundedCornerShape(12.dp))
                ) {
                    if (text.isEmpty() && !isFocused) {
                        Text(
                            text = placeHolder,
                            color = OldSilverColor,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterStart)
                                .padding(horizontal = 40.dp)
                        )
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 40.dp)
                            .align(alignment = Alignment.CenterStart)
                    ){
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = OldSilverColor,
                            modifier = Modifier.height(height = 22.dp).width(width = 32.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 10.dp)
                        )
                        BasicTextField(
                            value = text,
                            onValueChange = onTextChange,
                            textStyle = TextStyle(
                                color = MaastrichtBlueColor,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier
                                .weight(weight = 1f)
                                .background(Color.Transparent)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(horizontal = 8.dp)
                                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                            singleLine = true
                        )
                        if(text.isNotEmpty()) {
                            Icon(
                                painter = painterResource(resource = Res.drawable.ic_clear_text),
                                contentDescription = null,
                                modifier = Modifier.height(height = 22.dp).width(width = 26.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(start = 2.dp, end = 12.dp)
                                    .clickable { onTextChange("") },
                                tint = SettingsIconColor
                            )
                        }
                    }

                }
            }
            if(isFocused) {
                Text(
                    text = stringResource(Res.string.CancelSearch),
                    style = Typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = AccentColor
                    ),
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 4.dp)
                        .align(alignment = Alignment.CenterVertically)
                        .clickable {
                            focusManager.clearFocus()
                        }
                )
            }
        }
    }
}