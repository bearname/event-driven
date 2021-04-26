// import logo from './logo.svg'
import './App.css'

import Main from './pages/Main'
import Statistic from './pages/Statistic'
import TextDetails from './pages/TextDetails'

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

// function Home() {
//   return <h2>Home</h2>;
// }

// function About() {
//   return <h2>About</h2>;
// }

// function Users() {
//   return <h2>Users</h2>;
// }

function App() {
  return (
    <div className="App">
      <header>
        <Router>
          <div>
            <nav>
              <ul>
                <li>
                  <Link to="/">Home</Link>
                </li>
                {/* <li>
                  <Link to="/about">About</Link>
                </li>
                <li>
                  <Link to="/users">Users</Link>
                </li> */}
                <li>
                  <Link to="/stats">Stats</Link>
                </li>
                <li>
                  <Link to="/job/new">New job</Link>
                </li>
              </ul>
            </nav>

            {/* A <Switch> looks through its children <Route>s and
                renders the first one that matches the current URL. */}
            <Switch>
              {/* <Route path="/about">
                <About />
              </Route>
              <Route path="/users">
                <Users />
              </Route> */}
              <Route path="/stats">
                <Statistic />
              </Route>
              <Route path="/job/new">
                <TextDetails />
              </Route>
              <Route path="/">
                <Main />
              </Route>
            </Switch>
          </div>
        </Router>
      </header>
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header> */}
    </div>
  );
}

export default App
