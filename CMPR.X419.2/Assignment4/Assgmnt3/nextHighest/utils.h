/*
 * utils.h
 */
#ifndef UTILS_H_
#define UTILS_H_

#include "node.h"

struct node_t * createListFromArray(int *vals, size_t size, int initNextHighest);
void printList(struct node_t *list);
void deleteList(struct node_t *list);

#endif /* UTILS_H_ */
