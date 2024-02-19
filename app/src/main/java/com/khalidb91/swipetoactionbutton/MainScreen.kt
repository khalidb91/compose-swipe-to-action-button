package com.khalidb91.swipetoactionbutton

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khalidb91.SwipeToActionButton.SwipeToActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

val isProcessing: MutableStateFlow<Boolean> = MutableStateFlow(false)
val isCompleted: MutableStateFlow<Boolean> = MutableStateFlow(false)
val isSwiped: MutableState<Boolean> = mutableStateOf(false)


@Composable
fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val coroutineScope = rememberCoroutineScope()
        val isProcessing by isProcessing.collectAsState(false)
        val isComplete by isCompleted.collectAsState(false)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text("Swipe to perform the action")

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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { reset() }
            ) {
                Text(text = "Rest")
            }

        }

    }

}

private fun reset() {
    GlobalScope.launch(Dispatchers.IO) {
        isProcessing.emit(false)
        isCompleted.emit(false)
        isSwiped.value = false
    }
}

private fun longWork() {
    GlobalScope.launch(Dispatchers.IO) {
        isProcessing.emit(true)

        delay(2000)
        isCompleted.emit(true)

        isProcessing.emit(false)
    }
}