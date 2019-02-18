#include <stdio.h>
#include <stdlib.h>

struct node_t {
  struct node_t *next;
  struct node_t *nextHighest;
  int val;
};

struct node_t * createListFromArray(int *vals, size_t size, int initNextHighest) {
  struct node_t *nextNode = NULL;
  struct node_t *hi = NULL;
  for (int i = size-1 ; i >=0 ; i--) {
    struct node_t *node = (struct node_t*)malloc(sizeof(struct node_t));
    node->next = nextNode;
    if (initNextHighest) {
      node->nextHighest = hi;
    } else {
      node->nextHighest = NULL;
    }
    node->val = vals[i];
    nextNode = node;
    if (hi) {
      if (node->val > hi->val) {
        hi = node;
      }
    } else {
      hi = node;
    }
  }
  return nextNode;
}

void printList(struct node_t *list) {
  struct node_t *nextNode = list;
  while(nextNode) {
    printf("node: val %d",nextNode->val);
    if (nextNode->nextHighest) {
      printf(", nextHighest %d",nextNode->nextHighest->val);
    }
    printf("\n");
    nextNode = nextNode->next;
  }
}

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

int main() {
  printf("Hello\n");
  //int a[] = {1,2,3,4};
  //int a[] = {3,10,1,7,5,4,2,9,8,6};
  //int a[] = {4,6,7,9,10,2,8,1,3,5};
//  int a[] = {5,1,9,8,10,4,6,3,2,7};
  //int a[] = {10,7,2,3,4,1,6,8,9,5};
  //int a[] = {6,3,7,8,4,5,1,2,9,10};
  //int a[] = {10,7,2,3,9,4,1,6,8,9,5};
  //int a[] = {-3,10,-10,3,-5,4,7,9,-9,-7,2,5,-8,6,-6,-4,0,-2,1,-1,8};
  int a[] = {2,7,-6,-2,1,-9,3,10,4,5,9,-7,-1,8,-3,-5,-10,6,-4,0,-8};
  struct node_t *list = createListFromArray(a, sizeof(a)/sizeof(a[0]), 0);
  initNextHighest(list);
  printList(list);
  return 0;
}



