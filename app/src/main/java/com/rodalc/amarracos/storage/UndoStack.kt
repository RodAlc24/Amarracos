package com.rodalc.amarracos.storage

class UndoStack<T> public constructor(private val maxCapacity: Int = 0) {
    private var stack: MutableList<T> = mutableListOf()

    fun push(element: T) {
        if (maxCapacity == 0 || stack.size < maxCapacity) {
            stack.add(element)
        } else {
            stack.removeFirst()
            stack.add(element)
        }
    }

    fun size(): Int {
        return stack.size
    }

    fun pop(): T? {
        return if (stack.isEmpty()) null else stack.removeLast()
    }
}