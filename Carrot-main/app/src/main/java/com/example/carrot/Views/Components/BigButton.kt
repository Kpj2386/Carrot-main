package com.example.carrot.Views.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrot.R

@Composable
fun BigButton(text: String, image: Int? = R.drawable.cart_logo, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(400.dp)
            .padding(horizontal = 16.dp).then(modifier),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,

                //Text(text = text, color = Color.Black)


                fontSize = 16.sp,
                modifier = Modifier.padding(end = 8.dp),
                color= Color.Black
            )
            if (image != null) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Button Image",
                    modifier = Modifier.height(24.dp)
                )
            }
        }

    }
}