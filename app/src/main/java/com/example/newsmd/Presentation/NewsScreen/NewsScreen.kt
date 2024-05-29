package com.example.newsmd.Presentation.NewsScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsmd.Presentation.Components.BottomSheet
import com.example.newsmd.Presentation.Components.CategoryTab
import com.example.newsmd.Presentation.Components.NewsArticleCard
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(
    state: NewsState,
    onEvent: (NewsScreenEvent) -> Unit,
    //onReadFullStoryButtonClick:(String) -> Unit
) {
    val scrollHide = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val coroutineScope = rememberCoroutineScope()
    val categories = listOf(
        "General","Business","Health","Science","Sports","Technology","Entertainment"
    )

    val pagerState = rememberPagerState(pageCount = {categories.size})

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var shouldBottomSheetShow by remember { mutableStateOf(false) }
    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            onDismissRequest = { shouldBottomSheetShow = false },
            sheetState = sheetState,
            content = {
                state.selectedArticle?.let {
                    BottomSheet(
                        article = it,
                        onReadFullStoryButtonClicked = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) shouldBottomSheetShow = false
                            }
                        }
                    )
                }
            }
        )
    }



    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onEvent(NewsScreenEvent.OnCategoryChanged(category = categories[page]))
        }
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollHide.nestedScrollConnection),
        topBar = { ScreenTopBar(
            scrollBehavior = scrollHide
        )

        }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CategoryTab(
                pagerState = pagerState,
                categories = categories,
                onTabSelected = {index -> coroutineScope.launch { pagerState.animateScrollToPage(index) } }
            )
            HorizontalPager(
                state = pagerState
            ) {page->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .graphicsLayer {
                            val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                            // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                            translationX = pageOffset * size.width
                            // apply an alpha to fade the current page in and the old page out
                            alpha = 1 - pageOffset.absoluteValue
                        }


                ) {
                    items(state.articles) { article ->
                        NewsArticleCard(article = article, onCardClicked = { article ->
                            shouldBottomSheetShow = true
                            onEvent(NewsScreenEvent.OnNewsCardClicked(article = article))
                        }
                        )
                    }
                }
            }

        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

