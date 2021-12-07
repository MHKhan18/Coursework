import React, { useEffect, useRef, useState } from 'react';
import io from 'socket.io-client';
import './App.css';

function App() {
  const [state, setState] = useState({ message: '', name: '' });
  const [chat, setChat] = useState([]);

  const [rooms, setRooms] = useState(["JavaScript", "Python", "Java"]);
  const [selectedRoom, setSelectedRoom] = useState("Python");

  const socketRef = useRef();

  useEffect(() => {
    socketRef.current = io('/');
    return () => {
      socketRef.current.disconnect();
    };
  }, []);

  useEffect(() => {
    socketRef.current.on('message', ({ name, message }) => {
      setChat([...chat, { name, message }]);
    });
    socketRef.current.on('user_join', function (data) {
      setChat([
        ...chat,
        { name: 'ChatBot', message: `${data} has joined the chat` }
      ]);
    });
  }, [chat]);

  const userjoin = (name, room) => {
    socketRef.current.emit('user_join', { name, room });
  };

  const onMessageSubmit = (e) => {
    let msgEle = document.getElementById('message');
    console.log([msgEle.name], msgEle.value);
    setState({ ...state, [msgEle.name]: msgEle.value });
    socketRef.current.emit('message', {
      name: state.name,
      message: msgEle.value,
      room: selectedRoom
    });
    e.preventDefault();
    setState({ message: '', name: state.name });
    msgEle.value = '';
    msgEle.focus();
  };

  const onSelectRoom = (e) => {
    setSelectedRoom(e.target.value);
    // console.log('Room selected:', e.target.value);
  }

  const onLeave = () => {
    socketRef.current.emit('leave', { name: state.name, room: selectedRoom });
    setState({ message: '', name: '' });

  }

  const onCreateRoom = (e) => {
    e.preventDefault();
    setRooms([...rooms, document.getElementById('room_input').value]);
    document.getElementById('room_input').value = '';
  }

  const renderChat = () => {
    return chat.map(({ name, message }, index) => (
      <div key={index}>
        <h3>
          {name}: <span>{message}</span>
        </h3>
      </div>
    ));
  };

  return (
    <div>
      {state.name && (
        <div className="card">
          <div className="render-chat">
            <h1>{`${selectedRoom} Room`}</h1>
            {renderChat()}
          </div>
          <form onSubmit={onMessageSubmit}>
            <h1>Messenger</h1>
            <div>
              <input
                name="message"
                id="message"
                variant="outlined"
                label="Message"
              />
            </div>
            <button>Send Message</button>
          </form>
          <button onClick={onLeave}>Leave Room</button>
        </div>
      )}

      {!state.name && (
        <div>
          <form
            className="form"
            onSubmit={(e) => {
              console.log(document.getElementById('username_input').value);
              e.preventDefault();
              setState({ name: document.getElementById('username_input').value });
              userjoin(document.getElementById('username_input').value, selectedRoom);
              // userName.value = '';
            }}
          >
            <div className="form-group">
              <label>
                User Name:
                <br />
                <input id="username_input" required />
              </label>
              <br />
              <label>
                Room:
                <br />
                <select
                  value={selectedRoom}
                  onChange={onSelectRoom}
                >
                  {rooms.map((room, index) => {
                    return (<option value={room}>{room}</option>)
                  })}
                </select>
              </label>
            </div>
            <br />

            <br />
            <br />
            <button type="submit"> Click to join</button>
          </form>

          <form className="form" onSubmit={(e) => onCreateRoom(e)}>
            <div className="form-group">
              <label>
                New Room:
                <br />
                <input id="room_input" required />
              </label>
            </div>
            <button type="submit">Create Room</button>
          </form>
        </div >
      )
      }
    </div >
  );
}

export default App;
