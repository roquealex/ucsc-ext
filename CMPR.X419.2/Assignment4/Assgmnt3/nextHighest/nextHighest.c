#include <stdio.h>
#include <assert.h>
#include "node.h"
#include "utils.h"

// Solution:
void initNextHighest(struct node_t *list) {
  if(list) {
    list->nextHighest = list->next;
    if (list->next) {
      initNextHighest(list->next);
      if(list->next->nextHighest && list->next->nextHighest->val > list->next->val) {
        list->nextHighest = list->next->nextHighest;
      }
    }
  }
}

#define NUM_TESTS 10
int main() {

  int test0[] = {2,7,-6,-2,1,-9,3,10,4,5,9,-7,-1,8,-3,-5,-10,6,-4,0,-8};
  int test1[] = {1,2,3,4};
  int test2[] = {3,10,1,7,5,4,2,9,8,6};
  int test3[] = {4,6,7,9,10,2,8,1,3,5};
  int test4[] = {5,1,9,8,10,4,6,3,2,7};
  int test5[] = {10,7,2,3,4,1,6,8,9,5};
  int test6[] = {6,3,7,8,4,5,1,2,9,10};
  int test7[] = {10,7,2,3,9,4,1,6,8,9,5};
  int test8[] = {-1000,1000,2000,500,400};
  int test9[] = {0};

  int *test[NUM_TESTS] = {
    test0,test1,test2,test3,test4,
    test5,test6,test7,test8,test9
  };

  int testSize[NUM_TESTS] = {
    sizeof(test0)/sizeof(int), sizeof(test1)/sizeof(int),
    sizeof(test2)/sizeof(int), sizeof(test3)/sizeof(int),
    sizeof(test4)/sizeof(int), sizeof(test5)/sizeof(int),
    sizeof(test6)/sizeof(int), sizeof(test7)/sizeof(int),
    sizeof(test8)/sizeof(int), sizeof(test9)/sizeof(int)
  };

  for (int i = 0 ; i < NUM_TESTS; i++) {
    int *a = test[i];
    int aSize = testSize[i];
    struct node_t *ptr, *ptrGold;
    struct node_t *list = createListFromArray(a, aSize, 0);
    struct node_t *golden = createListFromArray(a, aSize, 1);
    initNextHighest(list);
    printList(list);

    ptr = list;
    ptrGold = golden;
    while (ptr && ptrGold) {
      assert(ptr->val == ptrGold->val);
      if (ptr->nextHighest) {
        assert(ptrGold->nextHighest);
        assert(ptr->nextHighest->val == ptrGold->nextHighest->val);
      } else {
        assert(!ptrGold->nextHighest);
      }
      ptr = ptr->next;
      ptrGold = ptrGold->next;
    }
    assert(ptr==NULL);
    assert(ptrGold==NULL);

    deleteList(list);
    deleteList(golden);
    list = NULL;
    golden = NULL;
  }
  printf("Test PASSED\n");
  return 0;
}



