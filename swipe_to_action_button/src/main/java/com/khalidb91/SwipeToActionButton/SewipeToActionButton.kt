package com.khalidb91.SwipeToActionButton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.khalidb91.swipe_to_action_button.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class Anchor(val fraction: Float) {
    Start(0f),
    End(1f)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToActionButton(
    modifier: Modifier = Modifier,
    text: String,
    progressText: String,
    isComplete: Boolean,
    isSwiped: MutableState<Boolean> = mutableStateOf(false),
    enabled: Boolean = true,
    doneImageVector: ImageVector = Icons.Rounded.Done,
    startColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    endColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(8.dp),
    action: () -> Unit
) {

    val density = LocalDensity.current
    val indicatorSize = 50.dp
    val indicatorSizePx = with(density) { indicatorSize.toPx() }

    val (swipeComplete, setSwipeComplete) = remember { isSwiped }

    val swipeState = remember {
        AnchoredDraggableState(
            initialValue = Anchor.Start,
            positionalThreshold = { distance: Float -> distance * 0.98f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
            confirmValueChange = { anchor ->
                if (anchor == Anchor.End) {
                    setSwipeComplete(true)
                    action()
                }
                false
            }
        )
    }


    val background: Brush = if (swipeComplete) {
        Brush.horizontalGradient(
            colors = listOf(endColor, endColor)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(endColor, startColor),
            startX = 0f,
            endX = 1f
        )
    }

    val textStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )

    val blinkingAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BlinkingAlpha"
    )

    val textAlpha = if (swipeComplete) blinkingAlpha else 1f
    val hintText = if (swipeComplete) progressText else text


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { layoutSize: IntSize ->
                val dragEndPoint = layoutSize.width - indicatorSizePx
                swipeState.updateAnchors(
                    DraggableAnchors {
                        Anchor.entries.forEach { anchor ->
                            anchor at dragEndPoint * anchor.fraction
                        }
                    }
                )
            }
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .animateContentSize()
            .requiredHeight(indicatorSize),
    ) {

        AnimatedVisibility(
            visible = isComplete.not(),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {

            Text(
                text = hintText.uppercase(),
                textAlign = TextAlign.Center,
                style = textStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .alpha(textAlpha)
            )

        }

        AnimatedVisibility(
            visible = isComplete,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {

            Icon(
                imageVector = doneImageVector,
                contentDescription = null,
                tint = White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp),
            )

        }

        if (swipeComplete.not()) {

            Thumb(
                modifier = Modifier
                    .anchoredDraggable(
                        state = swipeState,
                        orientation = Orientation.Horizontal,
                        reverseDirection = false,
                        enabled = enabled
                    )
                    .size(indicatorSize)
                    .align(Alignment.CenterStart)
                    .offset {
                        IntOffset(
                            x = if (swipeComplete && isComplete) {
                                0
                            } else {
                                swipeState.requireOffset().roundToInt()
                            },
                            y = 0
                        )
                    },
                shape = shape
            )

        }

    }

}

@Preview
@Composable
fun SwipeToActionButtonPreview() {

    val coroutineScope = rememberCoroutineScope()

    val (isComplete, setIsComplete) = remember {
        mutableStateOf(false)
    }

    SwipeToActionButton(
        modifier = Modifier.padding(20.dp),
        text = "SWIPE FOR MAGIC",
        progressText = "Working...",
        isComplete = isComplete,
        action = {
            coroutineScope.launch {
                delay(2000)
                setIsComplete(true)
            }
        },
        shape = RoundedCornerShape(8.dp),
    )

}

@Composable
fun Thumb(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .padding(0.dp)
            .shadow(elevation = 2.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.rounded_arrow_circle_right_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp),
        )
    }
}

@Preview
@Composable
fun ThumbPreview() {
    Box {
        Thumb(modifier = Modifier.padding(8.dp))
    }
}


