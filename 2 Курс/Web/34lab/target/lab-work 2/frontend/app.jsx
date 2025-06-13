const { Provider } = ReactRedux;
const { connect } = ReactRedux;

const mapStateToProps = state => ({
    user: state.user
});

const App = ({ user }) => (
    <div className="container">
        {user ? <MainPage /> : <LoginPage />}
    </div>
);

const ConnectedApp = connect(mapStateToProps)(App);

ReactDOM.render(
    <Provider store={store}>
        <ConnectedApp />
    </Provider>,
    document.getElementById('root')
);