/*
 * solution.cpp
 */
#include <vector>
#include <algorithm>
#include <cassert>
#include "node.h"

/*
node_t *initNextHigher(node_t *list) {
  // Build a heap with node pointers:
  std::vector<node_t*> node_heap;
  node_t *ptr = list;
  while(ptr) {
    node_heap.push_back(ptr);
    ptr = ptr->next;
  }
  auto compNode = [](node_t* a, node_t* b) {return a->val < b->val;};
  make_heap(node_heap.begin(), node_heap.end(),compNode);

  // Fill the next higher poping from the heap
  ptr = nullptr;
  while(!node_heap.empty()) {
    node_heap.front()->next_higher = ptr;
    ptr = node_heap.front();
    pop_heap(node_heap.begin(), node_heap.end(),compNode);
    node_heap.pop_back();
  }
  return ptr;
}
*/
//5->4->7->null
static node_t *qsortNextHigher(node_t *start, node_t *end) {
  if (!start) return start;
  if (start==end) return start;
  node_t *pivot = start;
  node_t *left = pivot;
  start = start->next_higher;
  // Bug : pivot should start pointing to the end otherwise will be disconnected
  pivot->next_higher = end;
  while (start != end) {
    node_t *next_higher = start->next_higher;
    if (start->val > pivot->val) {
      start->next_higher = pivot->next_higher;
      pivot->next_higher = start;
    } else {
      start->next_higher = left;
      left = start;
    }
    start = next_higher;
  }
  left = qsortNextHigher(left, pivot);
  node_t *right = qsortNextHigher(pivot->next_higher,nullptr);
  // Bug: the pivot has to be reconected to the right side
  pivot->next_higher = right;

  return left;
}

static node_t *divideNextHigher(node_t *list) {
  node_t *fast = list;
  node_t *slow = list;

  assert(list);
  int count = 0;
  while(fast!= nullptr) {
    fast = fast->next_higher;
    count++;
    // Bug this was never tested as what it was supposed to do:
    if (count==2 && fast) {
      slow = slow->next_higher;
      count = 0;
    }
  }
  // break wherever we are
  fast = slow->next_higher;
  slow->next_higher = nullptr;
  return fast;

}
static node_t *mergeNextHigher(node_t *list1, node_t *list2) {
  node_t *head, *tail, *rem;
  head = tail = nullptr;
  while( list1 && list2 ) {
    node_t *lower;
    if (list1->val > list2->val) {
      lower = list2;
      list2 = list2->next_higher;
    } else {
      lower = list1;
      list1 = list1->next_higher;
    }
    if (head == nullptr) {
      head = tail = lower;
    } else {
      tail->next_higher = lower;
      tail = lower;
    }
  }
  rem = (list1)?list1:list2;
  if (tail) {
    tail->next_higher = rem;
  } else {
    head = rem;
  }
  return head;

}

static node_t *msortNextHigher(node_t *list) {
  if (list == nullptr || list->next_higher==nullptr) return list;
  node_t *upper = divideNextHigher(list);
  node_t *l1 = msortNextHigher(list);
  node_t *l2 = msortNextHigher(upper);
  return mergeNextHigher(l1,l2);
}

node_t *initNextHigher(node_t *list) {
  if (list==nullptr) return list;
  for (node_t *node = list; node != nullptr ; node = node->next) {
    node->next_higher = node->next;
  }
  //return qsortNextHigher(list,nullptr);
  return msortNextHigher(list);
}




