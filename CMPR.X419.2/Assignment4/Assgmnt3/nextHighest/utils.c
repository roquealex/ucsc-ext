/*
 * utils.c
 */
#include <stdio.h>
#include <stdlib.h>
#include "node.h"
#include "utils.h"

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

void deleteList(struct node_t *list) {
  if (list) {
    deleteList(list->next);
    free(list);
  }
}
