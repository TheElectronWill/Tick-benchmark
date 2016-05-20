/*
 * Copyright (C) 2015 TheElectronWill
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.electronwill.tests.thread;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A Thread-safe implementation of Bag.
 *
 * @author TheElectronWill
 * @param <E>
 */
public class SynchronizedBag<E> extends AbstractCollection<E> implements Bag<E> {

	private class BagIterator implements Iterator<E> {

		private int pos = 0;

		@Override
		public boolean hasNext() {
			return pos < size;
		}

		@Override
		public E next() {
			return (E) array[pos++];
		}

		@Override
		public void remove() {
			SynchronizedBag.this.remove(pos);
		}

	}

	private Object[] array;
	private int size = 0;

	private final int capacityIncrement;

	/**
	 * Constructs a new SynchronizedBag with an initial capacity of ten and an increment of 2.
	 */
	public SynchronizedBag() {
		this(10, 2);
	}

	public SynchronizedBag(int initialCapacity) {
		this(initialCapacity, 2);
	}

	public SynchronizedBag(int initialCapacity, int capacityIncrement) {
		array = new Object[initialCapacity];
		this.capacityIncrement = capacityIncrement;
	}

	@Override
	public synchronized boolean add(E e) {
		if (size == array.length) {
			array = Arrays.copyOf(array, size + capacityIncrement);
		}
		array[size++] = e;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E o : c) {
			add(o);
		}
		return true;
	}

	@Override
	public synchronized void clear() {
		array = new Object[10];
	}

	@Override
	public synchronized boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			Object e = array[i];
			if (e.equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized void forEach(Consumer<? super E> action) {
		Objects.requireNonNull(action);
		for (E e : this) {
			if (e == null) {
				break;
			}
			action.accept(e);
		}
	}

	@Override
	public synchronized E get(int index) {
		if (index < size) {
			return (E) array[index];
		}
		throw new ArrayIndexOutOfBoundsException(index);
	}

	@Override
	public synchronized boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Creates and returns an iterator that iterates through all the elements of this SynchronizedBag.
	 *
	 * @deprecated the returned iterator is not Thread-safe, therefore you MUST use it inside a
	 * synchronized(*the bag
	 * instance*) block. The behavior is undefined if you use the iterator without synchronizing on this
	 * bag.
	 */
	@Deprecated
	@Override
	public synchronized Iterator<E> iterator() {
		return new BagIterator();
	}

	@Override
	public synchronized void remove(int index) {
		array[index] = array[--size];
		array[size] = null;
	}

	@Override
	public synchronized boolean remove(Object o) {
		for (int j = 0; j < array.length; j++) {
			Object element = array[j];
			if (element.equals(o)) {
				array[j] = array[--size];
				array[size] = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized boolean removeAll(Collection<?> c) {
		for (Object o : c) {
			remove(o);
		}
		return true;
	}

	@Override
	public synchronized boolean removeIf(Predicate<? super E> filter) {
		Objects.requireNonNull(filter);
		boolean removed = false;
		for (int i = 0; i < size; i++) {
			final E e = (E) array[i];
			if (e != null && filter.test(e)) {
				remove(i);
				removed = true;
			}
		}
		return removed;
	}

	@Override
	public synchronized boolean retainAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				remove(o);
			}
		}
		return true;
	}

	@Override
	public synchronized int size() {
		return size;
	}

	@Override
	public synchronized Object[] toArray() {
		return Arrays.copyOf(array, size);
	}

	@Override
	public synchronized void trimToSize() {
		array = Arrays.copyOf(array, size);
	}

	@Override
	public synchronized E tryGet(int index) {
		if (index < size) {
			return (E) array[index];
		}
		return null;
	}

}
