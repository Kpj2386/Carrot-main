package com.example.carrot.Views.LoginScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carrot.R
import com.example.carrot.ViewModels.LoginScreenViewModel
import com.example.carrot.ui.theme.PrimaryOrange

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.carrot.Helper.DisplayImageFromAssets
import com.example.carrot.NavRoutes
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }  // Detect clicks outside

    // Ensure the keyboard does NOT show when the screen first loads
    LaunchedEffect(Unit) {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
    }

    Box(
        modifier = Modifier

            .padding(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null  // Prevents ripple effect
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.cart_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
            )

            Text("Cart Checkout")
            val phoneNumber by viewModel.phoneNumber

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { it: String ->
                    val newPhoneNumber = it.filter { char -> char.isDigit() }
                    if (newPhoneNumber.length <= 10) {
                        viewModel.onPhoneNumberChange(newPhoneNumber)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.authenticateUser { isValid ->
                            if (isValid) {
                                viewModel.setUserDetails()
                                navController.navigate(NavRoutes.ROOT_SCREEN)
                            } else {
                                Toast.makeText(navController.context, "Please enter a valid phone number", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                ),
                label = { Text("Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .clearFocusOnKeyboardDismiss(),  // Ensure keyboard dismisses when tapping outside
                trailingIcon = {
                    Row(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(40.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    keyboardController?.hide()
                                    viewModel.authenticateUser { isValid ->
                                        if (isValid) {
                                            viewModel.setUserDetails()
                                            navController.navigate(NavRoutes.ROOT_SCREEN)
                                        } else {
                                            Toast.makeText(navController.context, "Please enter a valid phone number", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                .background(color = PrimaryOrange, shape = RoundedCornerShape(100)),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                        )
                    }
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            TextButton(onClick = { viewModel.loginWithEmailAction() }) {
                Text("Login with email?", color = Color.Black)
            }
        }
    }
}

/**
 * Extension function to clear focus when clicking outside the text field.
 */
@Composable
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier {
    val focusManager = LocalFocusManager.current
    return pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
}







