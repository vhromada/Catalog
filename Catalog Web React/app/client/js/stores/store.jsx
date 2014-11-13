goog.provide('app.stores.Store');

goog.require('app.stores.StoreRegistry');
goog.require('goog.events.EventTarget');

/**
 @param {app.stores.StoreRegistry} registry
 @constructor
 @extends {goog.events.EventTarget}
 */
app.stores.Store = function (registry) {
  goog.base(this);

  this.storeRegistry = registry;
  this.storeRegistry.addStore(this);

};
goog.inherits(app.stores.Store, goog.events.EventTarget);

app.stores.Store.prototype.notify = function () {
  this.dispatchEvent({type: 'change'});
};
