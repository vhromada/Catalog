goog.provide('app.games.Validator');

goog.require('goog.structs.Map');

/**
 * @constructor
 */
app.games.Validator = function () {

  /**
   * @type {goog.structs.Map}
   */
  this.errors = new goog.structs.Map();
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validate = function (game) {
  this.errors.clear();

  this.validateName(game);
  this.validateWikiEn(game);
  this.validateWikiCz(game);
  this.validateMediaCount(game);
  this.validateOtherData(game);
  this.validateNote(game);
};

/**
 * @param {app.games.Game} game
 * @returns {boolean}
 */
app.games.Validator.prototype.isValid = function (game) {
  this.validate(game);

  return this.errors.getCount() === 0;
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateName = function (game) {
  if (!game.name) {
    this.errors.set('name', 'Name is required.');
  } else if (game.name.length > 200) {
    this.errors.set('name', 'Maximum length of name is 200 characters.');
  }
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateWikiEn = function (game) {
  if (game.wikiEn && game.wikiEn.length > 100) {
    this.errors.set('wikiEn', 'Maximum length of URL to english Wikipedia is 100 characters.');
  }
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateWikiCz = function (game) {
  if (game.wikiCz && game.wikiCz.length > 100) {
    this.errors.set('wikiCz', 'Maximum length of URL to czech Wikipedia is 100 characters.');
  }
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateMediaCount = function (game) {
  if (!game.mediaCount) {
    this.errors.set('mediaCount', 'Count of media is required.');
  } else if (!/^[-]?[0-9]+$/.test(game.mediaCount)) {
    this.errors.set('mediaCount', 'Count of media must be number.');
  } else if (game.mediaCount <= 0) {
    this.errors.set('mediaCount', "Count of media must be positive number.");
  }
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateOtherData = function (game) {
  if (game.otherData && game.otherData.length > 100) {
    this.errors.set('otherData', 'Maximum length of other data is 100 characters.');
  }
};

/**
 * @param {app.games.Game} game
 */
app.games.Validator.prototype.validateNote = function (game) {
  if (game.note && game.note.length > 100) {
    this.errors.set('note', 'Maximum length of note is 100 characters.');
  }
};
