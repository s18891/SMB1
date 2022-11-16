package com.example.SMB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list_table")
data class ShoppingListItem(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String,
        var count: Int,
        var price: Double
)