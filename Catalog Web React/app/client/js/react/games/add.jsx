goog.provide('app.react.games.Add');

goog.require('app.games.Game');
goog.require('app.games.Validator');
goog.require('goog.string');

/**
 * @param {app.games.Store} store
 * @constructor
 */
app.react.games.Add = function (store) {
  var validator = new app.games.Validator();

  this.component = React.createFactory(React.createClass({
    getInitialState: function () {
      var game = new app.games.Game();
      return {game: game, submitted: false, valid: false}
    },
    render: function () {
      return (
        <div>
          <form className="form-horizontal" onSubmit={this.onFormSubmit}>
            {this.newTextField('name', 'Name')}
            {this.newTextField('wikiEn', 'URL to english Wikipedia')}
            {this.newTextField('wikiCz', 'URL to czech Wikipedia')}
            {this.newTextField('mediaCount', 'Count of media')}
            {this.newCheckBox('crack', 'Crack')}
            {this.newCheckBox('serialKey', 'Serial key')}
            {this.newCheckBox('patch', 'Patch')}
            {this.newCheckBox('trainer', 'Trainer')}
            {this.newCheckBox('trainerData', 'Data for trainer')}
            {this.newCheckBox('editor', 'Editor')}
            {this.newCheckBox('saves', 'Saves')}
            {this.newTextField('otherData', 'Other data')}
            {this.newTextField('note', 'Note')}
            <div className="form-group">
              <div className="col-sm-offset-2 col-sm-10">
                <button type="submit" className="btn btn-primary" disabled={this.state.valid}>Submit</button>
              </div>
            </div>
          </form>
        {this.newResultPanel()}
        </div>
      );
    },
    newTextField: function (key, label) {
      var id = "game-" + goog.string.toSelectorCase(key);
      var error = validator.errors.get(key);
      return (
        <div className={"form-group" + (error ? " has-error" : "")}>
          <label className="col-sm-2 control-label" htmlFor={id}>{label}</label>
          <div className="col-sm-5">
            <input id={id} name={key} type="text" className="form-control" disabled={this.state.valid} onChange={this.onFieldChange} value={this.state.game[key]} />
          </div>
          {this.newErrorField(error)}
        </div>
      );
    },
    newCheckBox: function (key, label) {
      var id = "game-" + goog.string.toSelectorCase(key);
      var error = validator.errors.get(key);
      return (
        <div className={"form-group" + (error ? " has-error" : "")}>
          <label className="col-sm-2 control-label" htmlFor={id}>{label}</label>
          <div className="col-sm-5">
            <input id={id} name={key} type="checkbox" disabled={this.state.valid} onChange={this.onCheckBoxChange} checked={this.state.game[key]}/>
          </div>
          {this.newErrorField(error)}
        </div>
      );
    },
    newErrorField: function (error) {
      if (error) {
        return (
          <div className="error">
            {error}
          </div>
        );
      }
    },
    onFieldChange: function (e) {
      var name = e.target.name;
      this.state.game[name] = e.target.value;
      if (this.state.submitted) {
        validator.validate(this.state.game);
      }
      this.forceUpdate();
    },
    newResultPanel: function () {
      if (this.state.valid) {
        return (
          <div className="result">Game was added.</div>
        );
      }
    },
    onCheckBoxChange: function (e) {
      var name = e.target.name;
      this.state.game[name] = e.target.checked;
      if (this.state.submitted) {
        validator.validate(this.state.game);
      }
      this.forceUpdate();
    },
    onFormSubmit: function (e) {
      e.preventDefault();
      if (this.state.valid) {
        return;
      }
      var valid = validator.isValid(this.state.game);
      if (valid) {
        this.state.game.mediaCount = goog.string.parseInt(this.state.game.mediaCount);
        store.add(this.state.game);
      }
      this.setState({submitted: true, valid: valid});
    }
  }));
};
