/*
 * node.h
 */
#ifndef NODE_H_
#define NODE_H_

struct node_t {
  struct node_t *next;
  struct node_t *nextHighest;
  int val;
};

#endif /* NODE_H_ */
