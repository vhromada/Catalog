goog.provide('app.stores.StoreRegistry');

goog.require('goog.Promise');
goog.require('goog.array');
goog.require('goog.events.EventTarget');

/**
 @constructor
 @extends {goog.events.EventTarget}
 */
app.stores.StoreRegistry = function () {
  goog.base(this);

  this.stores = [];

};
goog.inherits(app.stores.StoreRegistry, goog.events.EventTarget);

/**
 * @param {app.stores.Store} store
 */
app.stores.StoreRegistry.prototype.addStore = function (store) {
  this.stores.push(store);
  store.listen('change', this.notify.bind(this));
};

app.stores.StoreRegistry.prototype.notify = function () {
  this.dispatchEvent({type: 'change'});
};
