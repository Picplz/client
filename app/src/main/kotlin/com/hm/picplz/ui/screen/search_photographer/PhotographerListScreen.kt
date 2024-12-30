import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun PhotographerListScreen() {
    Column(
        modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Text(
            text = "주변 작가",
            style = MaterialTheme.typography.titleMedium,
            color = MainThemeColor.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(5) { index ->
                ListItem(
                    headlineContent = {
                        Text("작가 ${index + 1}")
                    },
                    supportingContent = {
                        Text("상세 정보...")
                    }
                )
            }
        }
    }
}
