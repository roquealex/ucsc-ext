/*
 * node.h
 */
#ifndef NODE_H_
#define NODE_H_

struct node_t {
  node_t *next;
  node_t *next_higher;
  int val;
};

#endif /* NODE_H_ */
