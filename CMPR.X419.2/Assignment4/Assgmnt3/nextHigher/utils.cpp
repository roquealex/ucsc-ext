// utils.cpp
#include <iostream>
#include <vector>
#include "node.h"
#include "utils.h"

node_t *createListFromVector(const std::vector<int> &vals) {
  node_t *nextNode = nullptr;
  for (auto rit = vals.crbegin() ; rit != vals.crend() ; ++rit) {
    node_t *node = new node_t;
    node->next = nextNode;
    node->next_higher = nullptr;
    node->val = *rit;
    nextNode = node;
  }
  return nextNode;
}

void printList(const node_t *list, bool ordered) {
  const node_t *nextNode = list;
  while(nextNode) {
    std::cout<<"node: val "<<nextNode->val<<std::endl;
    nextNode = (ordered)?nextNode->next_higher:nextNode->next;
  }
}

void deleteList(node_t *list) {
  if (list) {
    deleteList(list->next);
    delete list;
  }
}

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

