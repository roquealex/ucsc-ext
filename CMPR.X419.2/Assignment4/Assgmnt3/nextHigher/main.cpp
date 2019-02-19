#include <iostream>
#include <vector>

//using namespace std;

struct node_t {
  node_t *next;
  node_t *next_higher;
  int val;
};

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

void printList(const node_t *list, bool ordered = false) {
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

/*
bool compfunc(const int &a, const int &b){
  return (a<b);
}
*/

bool compNode(const node_t* a, const node_t* b){
  return (a->val<b->val);
}

node_t *initNextHigher(node_t *list) {
  // Build a heap with node pointers:
  std::vector<node_t*> node_heap;
  node_t *ptr = list;
  while(ptr) {
    node_heap.push_back(ptr);
    ptr = ptr->next;
  }
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

int main() {
  std::cout<<"Hello"<<std::endl;
  std::vector<int> vec = {1,2,3,6,7,4,5};
  node_t *list = createListFromVector(vec);

 //node_heap.push_back(6);
  //push_heap(int_heap.begin(), int_heap.end(),compfunc);
  //int_heap.push_back(6);

  node_t *ordered = initNextHigher(list);

  std::cout<<"Original:"<<std::endl;
  printList(list);
  std::cout<<"Ordered:"<<std::endl;
  printList(ordered,true);

  deleteList(list);
  list = nullptr;


  /*
  std::vector<int> int_heap;
  int_heap.push_back(2);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(3);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(6);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(7);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(5);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(4);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(8);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(10);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(9);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);
  int_heap.push_back(1);
  push_heap(int_heap.begin(), int_heap.end(),compfunc);

  while(!int_heap.empty()) {
    std::cout<<"Top of the heap: "<<int_heap.front()<<std::endl;
    pop_heap(int_heap.begin(), int_heap.end(),compfunc);
    int_heap.pop_back();
  }
  */

  return 0;
}





