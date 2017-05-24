package com.ahamed.multiviewadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.ahamed.multiviewadapter.util.PayloadProvider;
import java.util.Collection;
import java.util.List;

/**
 * DataGroupManger is a section of items with a header and items. It supports group level expansion
 * and collapsing
 *
 * @param <H> Refers to the header model
 * @param <M> Refers to the item model
 */
public class DataGroupManager<H, M> extends DataListUpdateManager<M> {

  private final DataItemManager<H> headerItemManager;
  private boolean isExpanded = false;

  public DataGroupManager(RecyclerAdapter adapter, H headerItem) {
    super(adapter);
    headerItemManager = new DataItemManager<>(adapter, headerItem);
  }

  public DataGroupManager(RecyclerAdapter adapter, H headerItem,
      @NonNull PayloadProvider<M> payloadProvider) {
    super(adapter, payloadProvider);
    headerItemManager = new DataItemManager<>(adapter, headerItem);
  }

  @Override void onGroupExpansionToggled() {
    isExpanded = !isExpanded;
    if (isExpanded) {
      adapter.notifyBinderItemRangeInserted(this, 1, getDataList().size());
    } else {
      adapter.notifyBinderItemRangeRemoved(this, 1, getDataList().size());
    }
  }

  @Override M getItem(int dataItemPosition) {
    return super.getItem(dataItemPosition - 1);
  }

  BaseDataManager getDataManagerForPosition(int itemPositionInManager) {
    if (itemPositionInManager == 0) {
      return headerItemManager;
    } else {
      return this;
    }
  }

  @Override int size() {
    return headerItemManager.size() + (isExpanded ? super.size() : 0);
  }

  public void setExpanded(boolean expanded) {
    isExpanded = expanded;
  }

  /**
   * Set header to the section. This will call the necessary {@link
   * RecyclerView.ItemAnimator}'s animation.
   *
   * @param item item to be added as header.
   */
  public final void setItem(M item) {
    if (getDataList().size() == 0) {
      getDataList().add(item);
      onInserted(0, 1);
    } else {
      getDataList().set(0, item);
      onChanged(0, 1, null);
    }
  }

  /**
   * Removes the entire group from the adapter. This will call the {@link
   * RecyclerView.ItemAnimator}'s remove animation.
   */
  public final void removeGroup() {
    if (getDataList().size() > 0) {
      getDataList().clear();
    }
    headerItemManager.removeItem();
  }

  /**
   * Appends the specified element to the end of {@link DataGroupManager}. Also calls {@link
   * RecyclerView.ItemAnimator}'s add animation.
   *
   * @param item element to be appended to this list
   * @return <tt>true</tt> (as specified by {@link Collection#add})
   * @throws UnsupportedOperationException if the <tt>add</tt> operation
   * is not supported by this {@link DataGroupManager}
   */
  public final boolean add(M item) {
    return add(item, isExpanded);
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this {@link DataGroupManager}, in the order that they are returned by the specified
   * collection's iterator (optional operation).  The behavior of this
   * operation is undefined if the specified collection is modified while
   * the operation is in progress.  (Note that this will occur if the
   * specified collection is this list, and it's nonempty.) Also calls {@link
   * RecyclerView.ItemAnimator}'s add animation.
   *
   * @param items collection containing elements to be added to this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
   * is not supported by this {@link DataGroupManager}
   * @see #add(Object)
   */
  public final boolean addAll(@NonNull Collection<? extends M> items) {
    return addAll(getDataList().size(), items, isExpanded);
  }

  /**
   * Inserts all of the elements in the specified collection into this
   * data manager at the specified position (optional operation).  Shifts the
   * element currently at that position (if any) and any subsequent
   * elements to the right (increases their indices).  The new elements
   * will appear in this list in the order that they are returned by the
   * specified collection's iterator.  The behavior of this operation is
   * undefined if the specified collection is modified while the
   * operation is in progress. Also calls {@link
   * RecyclerView.ItemAnimator}'s add animation.
   *
   * @param index index at which to insert the first element from the
   * specified collection
   * @param items collection containing elements to be added to this list
   * @return <tt>true</tt> if this data manager changed as a result of the call
   * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
   * is not supported by this data manager
   */
  public final boolean addAll(int index, @NonNull Collection<? extends M> items) {
    return addAll(index, items, isExpanded);
  }

  /**
   * Inserts the specified element at the specified position in this data manger
   * (optional operation).  Shifts the element currently at that position
   * (if any) and any subsequent elements to the right (adds one to their
   * indices). Also calls {@link
   * RecyclerView.ItemAnimator}'s add animation.
   *
   * @param index index at which the specified element is to be inserted
   * @param item element to be inserted
   * @throws UnsupportedOperationException if the <tt>add</tt> operation
   * is not supported by this data manger
   * @throws IndexOutOfBoundsException if the index is out of range
   * (<tt>index &lt; 0 || index &gt; size()</tt>)
   */
  public final void add(int index, M item) {
    add(index, item, isExpanded);
  }

  /**
   * Replaces the element at the specified position in this data manager with the
   * specified element (optional operation). Also calls necessary {@link
   * RecyclerView.ItemAnimator}'s animation with payload.
   *
   * @param index index of the element to replace
   * @param item element to be stored at the specified position
   * @throws UnsupportedOperationException if the <tt>set</tt> operation
   * is not supported by this data manager
   * @throws IndexOutOfBoundsException if the index is out of range
   * (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  public final void set(int index, M item) {
    set(index, item, isExpanded);
  }

  /**
   * Replaces the data list in this data manager with the new list. Also calls necessary {@link
   * RecyclerView.ItemAnimator}'s animation with payload.
   *
   * @param dataList list to be stored in the data manager
   */
  public final void set(List<M> dataList) {
    set(dataList, isExpanded);
  }

  /**
   * Removes the first occurrence of the specified element from this data manager,
   * if it is present (optional operation).  If this data manager does not contain
   * the element, it is unchanged.  More formally, removes the element with
   * the lowest index <tt>i</tt> such that
   * <tt>(item==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;item.equals(get(i)))</tt>
   * (if such an element exists).  If the data manager contained the element the element is removed
   * and {@link RecyclerView.ItemAnimator}'s remove animation will be called.
   *
   * @param item element to be removed from this data manager, if present
   */
  public final void remove(M item) {
    remove(item, isExpanded);
  }

  /**
   * Removes the element at the specified position in this list (optional operation).  Shifts any
   * subsequent elements to the left (subtracts one from their indices). If the element is removed
   * {@link RecyclerView.ItemAnimator}'s remove animation will be called.
   *
   * @param index the index of the element to be removed
   * @throws IndexOutOfBoundsException if the index is out of range
   * (<tt>index &lt; 0 || index &gt;= size()</tt>)
   */
  public final void remove(int index) {
    remove(index, isExpanded);
  }

  /**
   * Removes all of the elements from this data manager (optional operation).
   * The data manager will be empty after this call returns.  After the elements are removed
   * {@link RecyclerView.ItemAnimator}'s remove animation will be called.
   */
  public final void clear() {
    clear(isExpanded);
  }
}
