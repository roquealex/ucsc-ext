/*
 * solution.cpp
 */
#include <vector>
#include <algorithm>
#include "node.h"

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
