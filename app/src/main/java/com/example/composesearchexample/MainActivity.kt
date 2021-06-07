package com.example.composesearchexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composesearchexample.ui.theme.ComposeSearchExampleTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : ComponentActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSearchExampleTheme() {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Content()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Composable
    fun Content(searchModel: SearchModel = viewModel()) {
        var searchContent by remember { mutableStateOf("") }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.title))
                    }
                )
            }
        ) {
            Column {
                SearchEdit(
                    content = searchContent,
                    onContentChanged = {
                        searchContent = it.text
                        searchModel.queryByName(it.text)
                    },
                    Modifier.background(Color.White),
                )
                ContentList(searchContent = searchContent)
            }
        }
    }

    @Composable
    fun SearchEdit(
        content: String?,
        onContentChanged: (TextFieldValue) -> Unit,
        modifier: Modifier = Modifier
    ) {
        val focusRequester = remember { FocusRequester() }
        val selection = if (content.isNullOrEmpty()) {
            TextRange.Zero
        } else {
            TextRange(content.length, content.length)
        }
        Column(
            modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(4.dp))
        ) {
            OutlinedTextField(
                value = TextFieldValue(
                    text = content ?: "",
                    selection = selection
                ), onValueChange = onContentChanged, label = {
                    Text(
                        stringResource(id = R.string.hint_content),
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }, modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                onDispose { }
            }
        }
    }

    @Composable
    fun ContentList(searchContent: String?) {
        val searchModel: SearchModel = viewModel()
        val resource by searchModel.searchResult?.observeAsState()
        val scrollState = rememberLazyListState()
        resource?.let { list ->
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(list) { position, name ->
                    ListItem(searchContent = searchContent, name = name)
                }
            }
        }
    }

    @Composable
    fun ListItem(modifier: Modifier = Modifier, searchContent: String?, name: String) {
        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
            Text(
                buildAnnotatedString() {
                    name.forEach {
                        if (searchContent?.contains(it.toString()) == true) {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append(it)
                            }
                        } else {
                            append(it)
                        }
                    }
                }, modifier = modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            Divider()
        }
    }

}
