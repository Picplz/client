import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.data.model.NicknameFieldError
import com.hm.picplz.ui.theme.MainThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    placeholderStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Gray
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = Color.Black
    ),
    errors: List<NicknameFieldError> = emptyList(),
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: () -> Unit = {},
    shape: Shape = RoundedCornerShape(4.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    readOnly: Boolean = false,
    showError: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MainThemeColor.Olive,
        unfocusedBorderColor = MainThemeColor.Gray,
        focusedLabelColor = MainThemeColor.Olive,
        unfocusedLabelColor = MainThemeColor.Gray,
        focusedPlaceholderColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray
    )

    val isError = errors.isNotEmpty()

    BasicTextField(
        value = value,
        onValueChange = if (!readOnly) onValueChange else { _ -> },
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        singleLine = singleLine,
        interactionSource = interactionSource,
        enabled = enabled && !readOnly,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onAny = {
                keyboardActions()
            }
        ),
        textStyle = textStyle,
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = it,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            placeholder = {
                if (placeholder.isNotEmpty())
                    Text(
                        text = placeholder,
                        style = placeholderStyle,
                        modifier = Modifier.fillMaxWidth(),
                    )
            },
            supportingText = if (showError) {
                {
                    if (isError) {
                        Text(
                            text = errors.first().message,
                            fontSize = 12.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            } else null,
            contentPadding = OutlinedTextFieldDefaults.contentPadding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp,
                bottom = 8.dp
            ),
            colors = colors,
            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = shape,
                    unfocusedBorderThickness = 1.dp,
                    focusedBorderThickness = 2.dp
                )
            }
        )
    }
}