# SwipeToActionButton

`SwipeToActionButton` is a Jetpack Compose UI element that provides a swipeable button for triggering actions in your Android app.

## Installation

Add the following dependency to your app module's build.gradle file:

```
implementation 'com.khalidb91.swipetoactionbutton:swipetoactionbutton:1.0.0'
```

## Usage

To use SwipeToActionButton in your Compose project, follow these steps:

- Add the SwipeToActionButton to your Composable function:

    ```
    import com.khalidb91.swipetoactionbutton.SwipeToActionButton
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Shape
    import androidx.compose.ui.unit.dp

    @Composable
    fun MyScreen() {
        // Other Compose elements...

        SwipeToActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Swipe to trigger action",
            progressText = "Performing action...",
            isComplete = isComplete,
            isSwiped = isSwiped,
            action = {
                if (isProcessing.not()) {
                    coroutineScope.launch {
                        longWork()
                    }
                }
            },
            shape = CircleShape
        )

        // Other Compose elements...
    }
    ```

- Customize the parameters based on your requirements:

  modifier: Set the modifier for the SwipeToActionButton.
  text: Specify the text displayed on the button.
  progressText: Set the text to display during the action in progress.
  isComplete: Boolean to indicate if the action is complete.
  isSwiped: Boolean to indicate if the button has been swiped.
  action: Lambda function to execute when the button is swiped.
  shape: Set the shape of the button (e.g., CircleShape).

## Example

```
@Composable
fun MyScreen() {
    // Other Compose elements...

    SwipeToActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = "Swipe to trigger action",
        progressText = "Performing action...",
        isComplete = isComplete,
        isSwiped = isSwiped,
        action = {
            if (isProcessing.not()) {
                coroutineScope.launch {
                    longWork()
                }
            }
        },
        shape = CircleShape
    )

    // Other Compose elements...
}
```

## License

This library is distributed under the Apache License 2.0.



[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/khalidb91)