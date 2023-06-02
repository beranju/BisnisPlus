package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.constant.TimeRange
import com.beran.bisnisplus.ui.component.CustomDropDown
import com.beran.bisnisplus.utils.Utils

@Composable
fun BookCardSection(
    isLoading: Boolean,
    selected: String,
    onChangeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    incomeAmount: Double = 0.0,
    expenseAmount: Double = 0.0,
) {
    val difference = Utils.rupiahFormatter((incomeAmount - expenseAmount).toLong())
    val diffState = if (incomeAmount > expenseAmount) {
        "Untung"
    } else if (expenseAmount > incomeAmount) {
        "Rugi"
    } else {
        "Imbang"
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        if (!isLoading) {
        }
        ProfitLoseCard(
            incomeAmount = Utils.rupiahFormatter(incomeAmount.toLong()),
            expenseAmount = Utils.rupiahFormatter(expenseAmount.toLong()),
            difference = difference,
            stateText = diffState
        )
//        else {
//            ProfitLoseCardShimmer()
//        }
        DateDropDown(
            selected, onChangeSelected
        )
    }

}

@Composable
fun DateDropDown(
    selected: String,
    onChangeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = remember { TimeRange.values().map { it.range }.toList() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = "Pilih Tanggal",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(2f)
        )
        CustomDropDown(
            options = options,
            selectedValue = selected,
            onChangeValue = onChangeSelected,
            modifier = Modifier
                .weight(3f)
        )
    }
}
