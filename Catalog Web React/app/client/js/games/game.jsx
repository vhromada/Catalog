goog.provide('app.games.Game');

/**
 @constructor
 */
app.games.Game = function () {

  /**
   @type {number}
   */
  this.id;

  /**
   @type {string}
   */
  this.name;

  /**
   @type {string}
   */
  this.wikiEn;

  /**
   @type {string}
   */
  this.wikiCz;

  /**
   @type {number}
   */
  this.mediaCount;

  /**
   @type {boolean}
   */
  this.crack;

  /**
   @type {boolean}
   */
  this.serialKey;

  /**
   @type {boolean}
   */
  this.patch;

  /**
   @type {boolean}
   */
  this.trainer;

  /**
   @type {boolean}
   */
  this.trainerData;

  /**
   @type {boolean}
   */
  this.editor;

  /**
   @type {boolean}
   */
  this.saves;

  /**
   @type {string}
   */
  this.otherData;

  /**
   @type {string}
   */
  this.note;

  /**
   @type {number}
   */
  this.position;
};

/**
 @param {Object} json
 @return {app.games.Game}
 */
app.games.Game.loadFromJson = function (json) {
  var game = new app.games.Game();
  game.id = json.id;
  game.name = json.name;
  game.wikiEn = json.wikiEn;
  game.wikiCz = json.wikiCz;
  game.mediaCount = json.mediaCount;
  game.crack = json.crack;
  game.serialKey = json.serialKey;
  game.patch = json.patch;
  game.trainer = json.trainer;
  game.trainerData = json.trainerData;
  game.editor = json.editor;
  game.saves = json.saves;
  game.otherData = json.otherData;
  game.note = json.note;
  game.position = json.position;

  return game;
};
