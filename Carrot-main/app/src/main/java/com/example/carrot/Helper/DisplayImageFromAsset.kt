package com.example.carrot.Helper

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.carrot.R

@Composable
fun DisplayImageFromAssets(fileName: String, modifier: Modifier) {
    val context = LocalContext.current

    // Create an ImageLoader instance
    val imageLoader = ImageLoader(context)

    // Build the ImageRequest
    val imageRequest = ImageRequest.Builder(context)
        .data("file:///android_asset/$fileName") // Load from assets folder
        .placeholder(R.drawable.cart_logo) // Placeholder image
        .error(R.drawable.cart_logo) // Error image (if loading fails)
        .build()

    // Use rememberAsyncImagePainter with high-quality filtering
    val painter = rememberAsyncImagePainter(
        model = imageRequest,
        imageLoader = imageLoader,
        filterQuality = FilterQuality.High // Ensure high-quality scaling
    )

    // Display the image
    Image(
        painter = painter,
        contentDescription = "Product Image",
        modifier = modifier,
        contentScale = ContentScale.Inside // Adjust the scaling behavior
    )
}
