package com.rodalc.amarracos.storage

/**
 * Implementación de una pila con parámetros genéricos para la posibilidad de deshacer acciones.
 *
 * @param maxCapacity La capacidad máxima de la pila. Si es 0, la pila no tiene límite de tamaño.
 * @property T El tipo de elementos que la pila puede contener.
 */
class UndoStack<T>(private val maxCapacity: Int = 0) {
    private var stack: MutableList<T> = mutableListOf()

    /**
     * Agrega un elemento a la pila.
     *
     * Si la pila alcanza su capacidad máxima, se eliminará el primer elemento antes de agregar el nuevo.
     *
     * @param element El elemento a agregar a la pila.
     */
    fun push(element: T) {
        if (maxCapacity == 0 || stack.size < maxCapacity) {
            stack.add(element)
        } else {
            stack.removeFirst()
            stack.add(element)
        }
    }

    /**
     * Devuelve el tamaño actual de la pila.
     *
     * @return El número de elementos en la pila.
     */
    fun size(): Int {
        return stack.size
    }

    /**
     * Elimina y devuelve el último elemento de la pila.
     *
     * @return El último elemento de la pila, o null si la pila está vacía.
     */
    fun pop(): T? {
        return if (stack.isEmpty()) null else stack.removeLast()
    }
}
