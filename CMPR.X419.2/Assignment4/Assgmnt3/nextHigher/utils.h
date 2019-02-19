/*
 * utils.h
 */

#ifndef UTILS_H_
#define UTILS_H_

#include "node.h"

node_t *initNextHigher(node_t *list);

node_t *createListFromVector(const std::vector<int> &vals);

void printList(const node_t *list, bool ordered = false);

void deleteList(node_t *list);

#endif /* UTILS_H_ */
