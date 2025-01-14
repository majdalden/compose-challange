/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

fun toast(context: Context, s: String) {
    Toast
        .makeText(context, s, Toast.LENGTH_SHORT)
        .show()
}

// Start building your app here!
@Composable
fun MyApp() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = gray2)
    ) {
        HeaderSection(Modifier.align(Alignment.TopCenter))
        LoginSection(Modifier.align(Alignment.Center))
        FooterSection(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    TopImage(modifier)
    LogoImage(modifier)
}

@Composable
fun LoginSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SigInText(Modifier.align(alignment = Alignment.CenterHorizontally))
        EmailTextField(Modifier.align(alignment = Alignment.CenterHorizontally))
        PasswordTextField(Modifier.align(alignment = Alignment.CenterHorizontally))
        SignInButton(Modifier.align(alignment = Alignment.CenterHorizontally))
    }
}

@Composable
fun FooterSection(modifier: Modifier = Modifier) {
    BottomImage(modifier)
    NewAccountText(modifier)
}

@Composable
fun TopImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_background_top),
        contentDescription = "img_background_top",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (-44).dp)
            .height(height = 170.dp)
    )
}

@Composable
fun LogoImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_logo),
        contentDescription = "img_logo",
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Composable
fun AuthText(
    modifier: Modifier = Modifier,
    text: String = "",
    fontSize: TextUnit = 14.sp,
    color: Color = purple2,
    fontFamily: FontFamily = FontFamily(
        Font(
            resId = R.font.quicksand_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            color = color,
            fontFamily = fontFamily
        ),
        modifier = modifier
    )
}

@Composable
fun SigInText(modifier: Modifier = Modifier) {
    AuthText(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        text = stringResource(R.string.sign_in),
        fontSize = 29.sp,
        fontFamily = FontFamily(
            Font(
                resId = R.font.quicksand_bold,
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            )
        ),
    )
}

@Composable
fun EmailTextField(modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("") }

    AuthTextField(
        modifier = modifier,
        textValue = textValue,
        onValueChanged = {
            textValue = it
        },
        placeholder = stringResource(id = R.string.email),
        leadingIconPainter = painterResource(id = R.drawable.ic_person_outline_purple1_24),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )
}

@Composable
fun PasswordTextField(modifier: Modifier = Modifier) {
    var textValue by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    AuthTextField(
        modifier = modifier,
        textValue = textValue,
        onValueChanged = {
            textValue = it
        },
        placeholder = stringResource(id = R.string.password),
        leadingIconPainter = painterResource(id = R.drawable.ic_lock_outline_purple1_24),
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    if (passwordVisibility) painterResource(id = R.drawable.ic_baseline_eye_slash_24) else painterResource(
                        id = R.drawable.ic_baseline_eye_24
                    ),
                    contentDescription = "",
                    tint = purple2,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun SignInButton(modifier: Modifier = Modifier) {
    Button(
        onClick = {

        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = purple2)
    )
    {
        Icon(
            painterResource(id = R.drawable.ic_send_white_24),
            contentDescription = "Localized description",
            tint = Color.White,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}

@Composable
fun BottomImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_background_bottom),
        contentDescription = "img_background_bottom",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .offset(y = (100).dp)
//            .height(height = 130.dp)
    )
}

@Composable
fun NewAccountText(modifier: Modifier = Modifier) {
    val mContext = LocalContext.current
    AuthText(
        modifier = modifier
            .clickable {
                toast(mContext, "New Account")
            }
            .padding(all = 20.dp),
        text = stringResource(R.string.auth_new_account_message),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    textValue: String = "",
    onValueChanged: ((String) -> Unit)? = null,
    placeholder: String = "",
    leadingIconPainter: Painter? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIconPainter: Painter? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = textValue,
        onValueChange = onValueChanged ?: {},
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
            .border(
                width = 1.dp,
                color = gray3,
                RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50)),
        placeholder = {
            Text(
                fontSize = 16.sp,
                text = placeholder,
                color = gray1,
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.quicksand_regular
                    )
                )
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        leadingIcon = leadingIcon ?: {
            if (leadingIconPainter != null) {
                Icon(
                    painter = leadingIconPainter,
                    contentDescription = "Localized description",
                    tint = purple2,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        },
        trailingIcon = trailingIcon ?: {
            if (trailingIconPainter != null) {
                Icon(
                    painter = trailingIconPainter,
                    contentDescription = "Localized description",
                    tint = purple2,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        },
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
            onDone = {
                keyboardController?.hideSoftwareKeyboard()
                focusManager.clearFocus()
            }
        ),
        visualTransformation = visualTransformation
    )
}

