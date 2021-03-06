// main.cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>
#include "node.h"
#include "utils.h"
#include "solution.h"

int main() {
  std::cout<<"Test"<<std::endl;
  // Special case null
  node_t *result = (node_t *)0x124;
  result = initNextHigher(nullptr);
  assert(result==nullptr);

  //std::vector<int> vec = {0,0,0,3,3,3,3,-2,-2,-2,-2,4,4,4,-1,-1,8,1};
  std::vector<std::vector<int>> vectors = {
    {1,2,3,6,7,4,5},
    {8,9,2,3,10,6,7,1,5,4},
    {1,-2,4,9,5,-1,-9,2,8,-3,-10,-4,-7,7,-5,10,-8,-6,6,3,0},
    {0,0,0,3,3,3,3,-2,-2,-2,-2,4,4,4,-1,-1,8,1},
    {1,2},
    {0}
  };

  for (auto it = vectors.begin() ; it != vectors.end() ; ++it) {
    std::vector<int> &vec = *it;
    node_t *list = createListFromVector(vec);

    std::vector<int> ordvec(vec);
    sort(ordvec.begin(),ordvec.end());

    node_t *ordered = initNextHigher(list);

    std::cout<<"Original:"<<std::endl;
    printList(list);
    std::cout<<"Ordered:"<<std::endl;
    printList(ordered,true);

    // Check next ptr
    int i = 0;
    node_t *ptr = list;
    while(ptr) {
      //std::cout<<"vec:"<<vec[i]<<" ptr:"<<ptr->val<<std::endl;
      assert(vec[i]==ptr->val);
      ptr = ptr->next;
      i++;
    }

    // check next higher
    i = 0;
    ptr = ordered;
    while(ptr) {
      std::cout<<"vec:"<<ordvec[i]<<" ptr:"<<ptr->val<<std::endl;
      assert(ordvec[i]==ptr->val);
      ptr = ptr->next_higher;
      i++;
    }

    deleteList(list);
    list = nullptr;
  }

  return 0;
}

