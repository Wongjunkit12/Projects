import "./style.css";
import { fromEvent, interval, merge, timer} from 'rxjs'; 
import { map, filter, scan} from 'rxjs/operators';

function main(): void {
  /**
   * Inside this function you will use the classes and functions from rx.js
   * to add visuals to the svg element in pong.html, animate them, and make them interactive.
   *
   * Study and complete the tasks in observable examples first to get ideas.
   *
   * Course Notes showing Asteroids in FRP: https://tgdwyer.github.io/asteroids/
   *
   * You will be marked on your functional programming style
   * as well as the functionality that you implement.
   *
   * Document your code!
   */

  // Possible key presses for the game
  type Key = "KeyA" | "KeyD" | "KeyW" | "KeyS" | "KeyR" | "click" | "mousemove";

  // All key events
  type Event = "keydown" | "keyup" | "keypress" | "mousemove";

  // View HTML Element types
  type ViewType = "frog" | "car" | "plank" | "crocodile" | "fly";
  
  // Timer class to count how much time has elasped.
  class Tick { constructor(public readonly elapsed: number) {} };

  // Moving class to determine the movement of the object. Direction determins whether the object is moving up/left or down/right
  // and axis determine whether its moving on the x-axis or the y-axis.
  class Move { constructor(public readonly direction: number, public readonly axis: string) {} };

  // A reset class which will have a boolean signifying if the key to reset the game is pressed.
  class Reset { constructor(public readonly willReset: boolean) {} };

  // A class to signify whether to start the game.
  class Start { constructor(public readonly toStart: true) {} };

  // Game constants. Will never change
  const GAME_CONSTANTS = {
    // Canvas limits.
    CANVAS_SIZE: 600,
    CANVAS_ZERO: 0,
    // Pixel moved per frog step.
    FROG_STEP: 50,
    // X coordinates of all targets.
    TARGET_POS: [25, 125, 225, 325, 425, 525],
    // Total amount of targets. Each target number matches with the TARGET_POS for each index.
    TARGETS: [0, 1, 2, 3, 4, 5],
    // Y coordinates of each row.
    ROW_POS: [525, 475, 425, 375, 325, 275, 225, 175, 125, 75],
    // Default speeds of obstacles at game start.
    CAR_SPEED: [1.8, -3.8, 2.6, -4.6],
    PLANK_SPEED: [1.4, 1.6, 1],
    CROC_SPEED: [-1.6, -1.8],
    // Potential fly spawning coordinates
    FLY_X_SPAWN: [150, 450, 200, 550],
    FLY_Y_SPAWN: [475, 75, 275, 375]
  } as const;

  // Every object in a game is a MapItem, containing id, which points to the HTML element and their x and y coordinates currently.
  // vel is the number of pixels it moves per game tick or game action (eg: +1 moves 1 pixel to the right/down and -1 moves to the left/up)
  // axis is x for moving on the x axis and y for moving on the y axis. null means it's not moving.
  // width, height and colour is for the SVG element when creating obstacles.
  // rowsMoved is just an array of boolean detailing if the object has moved to that row (only for Frog)
  type MapItems = Readonly<{
    id: string,
    viewType: ViewType,
    x: number,
    y: number,
    vel: number,
    axis: string | null
    width: number,
    height: number,
    colour: string,
    rowsMoved: ReadonlyArray<boolean>,
    timeStepped: ReadonlyArray<number | string> // A paired array containing the time the frog stepped on a crocodile, 
                                                // and the crocodile object itself that it's standing on
  }>;

  // Game state. Contains all the states of the game's map items and also the required statistics.
  // time is time elapsed since game start, score is the current round score and high score is the highest score obtained in a round.
  // frog and fly are MapItems for frogger and the fly interactable respectively.
  // cars, planks and crocodiles are an array containing each obstacle MapItems object.
  type State = Readonly<{
    gameStart: boolean,
    time: number,
    round: number,
    score: number,
    highScore: number,
    frog: MapItems,
    cars: ReadonlyArray<MapItems>,
    planks: ReadonlyArray<MapItems>,
    crocodiles: ReadonlyArray<MapItems>,
    fly: MapItems,
    targetsFilled: ReadonlyArray<number>,   // The targets at the end for the frog to go in. Will be filled by the target numbers from 0 to 5.
    isDead: boolean,                        // Represents whether the frog has died and whether to restart the round or not.
    uniqueRows: ReadonlyArray<boolean>,     // Boolean Array of unique rows visited by frog. True if visited, false otherwise
    carSpeed: ReadonlyArray<number>,        // Arrays of each car current speed.
    plankSpeed: ReadonlyArray<number>,      // Arrays of each plank current speed.
    crocSpeed: ReadonlyArray<number>        // Arrays of each crocodile current speed.
  }>;

  const
    // Function to create the Frog's MapItems with spawning coordinates and 50 px per discrete step taken.
    createFrog = (): MapItems => {
      return ({
        id: "frog",
        viewType: "frog",
        x: 300,
        y: 575,
        vel: 50,
        axis: null,    // null to indicate it's not moving.
        width: 20,
        height: 20,
        colour: "green",
        // Array where each index represents a row and whether the frog has visited it before.
        // Will constantly update to true whenever frog visits that row.
        rowsMoved: [false, false, false, false, false, false, false, false, false],
        timeStepped: []
      });
    },

    // Function to create a obstacle based on the input.
    createObstacle = (itemViewType: ViewType) => (objId: string) => (xPos: number, yPos: number, velocity: number, width: number, height: number, colour: string): MapItems => {
      // If not creating a fly, set default obstacles parameters.
      return (itemViewType !== "fly" ? {
        id: itemViewType + objId,   // Id will be their viewTypes + a number. For example car + 1 means the id will be car1
        viewType: itemViewType,
        x: xPos,
        y: yPos,
        vel: velocity,
        axis: "x",        // Obstacles will only move on the x axis.
        width: width,
        height: height,
        colour: colour,
        rowsMoved: [],
        timeStepped: []
      } :
      // Else if it's a fly.
      {
        id: itemViewType + objId,   // Id will be their viewTypes + a number. For example car + 1 means the id will be car1
        viewType: itemViewType,
        x: xPos,
        y: yPos,
        vel: velocity,
        axis: null,        // For flies they do not move.
        width: width,
        height: height,
        colour: colour,
        rowsMoved: [],
        timeStepped: []
      });
    },

    // Curries the above createobstacle function with their respective ViewTypes.
    createCars = createObstacle("car"),
    createPlanks = createObstacle("plank"),
    createCrocodiles = createObstacle("crocodile"),
    createFlies = createObstacle("fly"),

    // Function to create all the obstacles MapItems in one row. Will create a row with specified amount of obstacles, the gap
    // between each obstacle, their row (y coordinates) and their velocity (how many pixels per game tick they are moving)
    // width, height and colour is used to create the SVG element later in updateView.
    // amount is how many cars in one row while total is how many total obstacles already exist for that object.
    // Takes in a function as it's parameter (higher order function) and calls a curried function to create the obstacles.
    createObstacles = (func: ((objId: string) => (xPos: number, yPos: number, velocity: number, width: number, height: number, colour: string) => MapItems), 
    x: number, total: number, amount: number, gap: number, row: number, velocity: number, width: number, height: number, colour: string): ReadonlyArray<MapItems> => {
      // Creates an empty array of length amount, maps over it and passes each index into the inputted fuction.
      return ([...Array(amount)]
      .map((_, i) => func(String(i + total))(x + (i * gap), row, velocity, width, height, colour)));
    },

    // Create multiple rows of cars based on a fixed input. speedIncrease default set to 0 if initialState
    // Input speedIncrease will increase the speed of all objects in the game for an increase in difficulty.
    createAllCars = (cars: ReadonlyArray<MapItems>, originalSpeed: ReadonlyArray<number>, speedIncrease: number = 0): ReadonlyArray<MapItems> => {
      const
        cars1 = cars.concat(createObstacles(createCars, GAME_CONSTANTS.CANVAS_ZERO - 100, 0, 3, 250, 500, originalSpeed[0] + speedIncrease, 75, 50, "red")),
        cars2 = cars1.concat(createObstacles(createCars, GAME_CONSTANTS.CANVAS_SIZE, 3, 1, 350, 450, originalSpeed[1] - speedIncrease, 150, 50, "red")),
        cars3 = cars2.concat(createObstacles(createCars, GAME_CONSTANTS.CANVAS_ZERO - 100, 4, 2, 500, 400, originalSpeed[2] + speedIncrease, 50, 50, "red")),
        cars4 = cars3.concat(createObstacles(createCars, GAME_CONSTANTS.CANVAS_SIZE, 6, 3, 150, 350, originalSpeed[3] - speedIncrease, 100, 50, "red"));
      return cars4;
    },

    // Create multiple rows of planks based on a fixed input. speedIncrease default set to 0 if initialState
    // Input speedIncrease will increase the speed of all objects in the game for an increase in difficulty.
    createAllPlanks = (planks: ReadonlyArray<MapItems>, originalSpeed: ReadonlyArray<number>, speedIncrease: number = 0): ReadonlyArray<MapItems> => {
      const
        planks1 = planks.concat(createObstacles(createPlanks, GAME_CONSTANTS.CANVAS_ZERO - 100, 0, 2, 300, 250, originalSpeed[0] + speedIncrease, 175 , 50, "brown")),
        planks2 = planks1.concat(createObstacles(createPlanks, GAME_CONSTANTS.CANVAS_SIZE, 2, 1, 100, 150, originalSpeed[1] + speedIncrease, 250, 50, "brown")),
        planks3 = planks2.concat(createObstacles(createPlanks, GAME_CONSTANTS.CANVAS_ZERO - 100, 3, 2, 300, 50, originalSpeed[2] + speedIncrease, 200, 50, "brown"));
      return planks3;
    },

    // Create multiple rows of crocodiles based on fixed input. speedIncrease default set to 0 if initialState
    // Input speedIncrease will increase the speed of all objects in the game for an increase in difficulty.
    createAllCrocs = (croc: ReadonlyArray<MapItems>, originalSpeed: ReadonlyArray<number>, speedIncrease: number = 0): ReadonlyArray<MapItems>=> {
      const
        croc1 = croc.concat(createObstacles(createCrocodiles, 600, 0, 4, 200, 200, originalSpeed[0] - speedIncrease, 150, 50, "yellow")),
        croc2 = croc1.concat(createObstacles(createCrocodiles, -100, 4, 3, 250, 100, originalSpeed[1] - speedIncrease, 175, 50, "yellow"));
      return croc2;
    },

    // Function to create a singly fly in the game and each round it will cycle through an array of potential spawning coordinates.
    createAllFlies = (round: number): MapItems => {
      return (createFlies("fly")(GAME_CONSTANTS.FLY_X_SPAWN[round], GAME_CONSTANTS.FLY_Y_SPAWN[round], 0, 15, 15, "orange"));
    },

    // Initial state of the game which it starts in. Time, score and highScore are set to 0 and all obstacles are created by
    // calling their respective function. The createFrog method is also called at game start.
    // Assign default values for the speed of obstacles from the GAME_CONSTANTS.
    initialState: State = {
      gameStart: false,
      time: 0,
      round: 0,
      score: 0,
      highScore: 0,
      frog: createFrog(),
      cars: createAllCars([], GAME_CONSTANTS.CAR_SPEED),
      planks: createAllPlanks([], GAME_CONSTANTS.PLANK_SPEED),
      crocodiles: createAllCrocs([], GAME_CONSTANTS.CROC_SPEED),
      fly: createAllFlies(0),
      targetsFilled: [],    // Target numbers will be added once it's filled.
      isDead: false,
      // An array where each index is a row and their values indicating whether the frogs has unique visited it before.
      // (will update true once frog has visited it at least once.)
      uniqueRows: [false, false, false, false, false, false, false, false, false],
      carSpeed: GAME_CONSTANTS.CAR_SPEED,
      plankSpeed: GAME_CONSTANTS.PLANK_SPEED,
      crocSpeed: GAME_CONSTANTS.CROC_SPEED
    },

    // A wrapper function so that when obstacles move out of canvas, they wrapped around to the other side.
    wrap = (pos: number): number => {
      // If the x coordinate is out of the canvas, add or subtract the coordinate with the canvas size (600) 
      // to wrap to the other side
      return (pos < -200 ? (pos + GAME_CONSTANTS.CANVAS_SIZE + 200)
        : (pos > GAME_CONSTANTS.CANVAS_SIZE ? (pos - GAME_CONSTANTS.CANVAS_SIZE - 200)
        : pos));
    },

    // Function that checks whether the input object (frog) is right infront of the target.
    isTarget = (obj: MapItems): boolean => {
      const objX = obj.x - 25;
      // Returns true if frog has any of the same x coordinates as any target.
      return (objX >= 25 && objX < 75) || (objX >= 125 && objX < 175) || (objX >= 225 && objX < 275) || (objX >= 325 && objX < 375) || (objX >= 425 && objX < 475) || (objX >= 525 && objX< 575);
    },

    // Function that checks if the frog has moved into that row before. True if it has, false if it hasn't. This is done to
    // increment score by 5 each unique row moved.
    updateRowsMoved = (frog: MapItems, newPosition: number): ReadonlyArray<boolean> => {
        // Will return an array of booleans indicating whether the frog has moved to that row.
        return (frog.rowsMoved.map((hasMoved, i) => (GAME_CONSTANTS.ROW_POS[i] === newPosition ? true : hasMoved)));
    },

    // Function that will occur if frog is right below the targets row.
    // Will check whether frog is colliding into a wall or into a target, prevent moving into wall. Allow moving into target.
    atExit = (s: State, frog: MapItems, newAxis: string | null, newPosition: number): MapItems => {
      return (frog.axis === "y" ? 
        // If it is moving into the endzone, check whether it's moving into the target slots or not
        (newPosition === 25 ?
        // If it is moving upwards into the target slots, allow movement. Else return the object itself for no movement.
        (isTarget(frog) ? {
          ...frog,
          y: newPosition,
          axis: newAxis,
          rowsMoved: updateRowsMoved(frog, newPosition)    // Update rows moved if y axis
        } :
        // If not moving at targets (moving into walls of target), disallow movement and return the object back.
        frog) : 
        // If moving downwards away from target slot, allow movement.
        {
          ...frog,
          y: newPosition,
          axis: newAxis,
          rowsMoved: updateRowsMoved(frog, newPosition)   // Update rows moved if y axis
        }) :
        // If the axis to move is x, change x coordinate to move on the x coord
        (frog.axis === "x" ? {
          ...frog, 
          x: newPosition,
          axis: newAxis
        } :
        // Else if null, do not move
        frog)
      );
    },

    // Function to move the frog specifically.
    moveFrog = (s: State, frog: MapItems, newAxis: string | null, newPosition: number): MapItems => {
      const
        // Checks if the frog is within the canvas, else it won't let it move there and go out of canvas.
        withinCanvas = (newPosition > GAME_CONSTANTS.CANVAS_ZERO ? newPosition < GAME_CONSTANTS.CANVAS_SIZE : false),
        // If the frog is on the y axis right before the exit zone.
        isAtExit =  frog.y === 75;

      // If frog is at the last row (in a target), reset frog position back to start 
      if (frog.y === 25) {
        return ({
          ...frog,
          x: 300,     // Resets frog position back to start
          y: 575,
          axis: newAxis,
          // Resets the rows visited so that frog can now gain 5 score per row again
          rowsMoved: [false, false, false, false, false, false, false, false, false]
        });
      }
      // If the frog is about to move on a crocodile
      else if (withinCanvas && frog.axis === "y" && (frog.y >= 75 && frog.y <= 275)) {
        const 
          specificCroc = frogOnFloatingObj(s, newPosition);
        return (newPosition === 25 ? atExit(s, frog, newAxis, newPosition) : 
        (specificCroc.length > 0 ? {
          ...frog,
          y: newPosition,
          axis: newAxis,
          rowsMoved: updateRowsMoved(frog, newPosition),    // Update rows moved if y axis
          timeStepped: [s.time, specificCroc[0].id]         // Saves the current state of time.
        } : {
          ...frog,
          y: newPosition,
          axis: newAxis,
          rowsMoved: updateRowsMoved(frog, newPosition),    // Update rows moved if y axis
          timeStepped: []                                   // If not moving on a crocodile, clear the array to "forget" the time
        }));
      }
      // Else If new position is within the canvas and not at the exit, move there by changing their coordinates and axis.
      else if (!isAtExit && withinCanvas) {
        // If the axis is x, change x coordinates.
        return (frog.axis === "x" ? {
          ...frog, 
          x: newPosition,
          axis: newAxis
        } :
        // Else if axis is y, change y coordinates.
        frog.axis === "y" ? {
          ...frog,
          y: newPosition,
          axis: newAxis,
          rowsMoved: updateRowsMoved(frog, newPosition)    // Update rows moved if y axis
        } :
        // Else if axis is null, just return the same object inputted as there will be no movement.
        frog);
      }
      // Else if the frog is at the exit (one row before the target)
      else if (isAtExit && withinCanvas) {
        // If the axis is y, check whether its moving into the endzone
        return atExit(s, frog, newAxis, newPosition);
      }
      // Else if new position is out of the canvas and is a frog, return the object back. (No movement)
      else {
        return frog;
      };
    },

    // Function to move the MapItems object. If the object axis to move is x, recreate the object but with the x property being 
    // in the new position. Else if it's y, do the same but for the y coordinate. Else if axis is a null, do not move so 
    // coordinates do not change.
    moveMapItems = (s: State, obj: MapItems): MapItems => {
      const 
        // Checks whether the map item being moved is the frog. If it is, change newAxis to null so that movement for frog will be 
        // executed once only.
        newAxis = (obj.id === "frog" ? null : obj.axis),
        // Checks whether the map item is moving. If it is, calcualte the new position based on their axis and velocity values.
        // If it's not moving, newPosition is it's old position so that it doesnt move.
        newPosition = (obj.axis !== null ? (obj.axis === "x" ? obj.x + obj.vel : obj.y + obj.vel) : (obj.axis === "x" ? obj.x : obj.y));
        
      // If current frog position is at the target slots, return it back to original position.
      if (obj.id === "frog") {
        return moveFrog(s, obj, newAxis, newPosition);
      }
      // If the object is not a frog and is instead the game obstacles,
      else {
        // If the axis is x, change x coordinates.
        return (obj.axis === "x" ? {
          ...obj, 
          x: wrap(newPosition)    // If object is moving out of canvas, wrap it around back to the other side.
        } :
        // Else if axis is y, change y coordinates.
        obj.axis === "y" ? {
          ...obj,
          y: wrap(newPosition)   // If object is moving out of canvas, wrap it around back to the other side.
        } :
        // Else if axis is null, just return the same object inputted as there will be no movement.
        obj);
      };
    },

    // A function that maps each MapItem in the input array to call the moveMapItems in order to move all of them based on their
    // coordinates.
    moveObstacles = (s: State, array: ReadonlyArray<MapItems>): ReadonlyArray<MapItems> => {
      // Will receive the obstacle array and then move each and every obstacle within that array.
      return (array.map((obj) => moveMapItems(s, obj)));
    },
    
    // Function that detects whether the frog has collided with a provided map item. Returns true if did, false otherwise.
    mapItemCollision = (frog: MapItems, item: MapItems): boolean => {
      const 
        // Gets the row the frog is currently in. Then check the space the frog is in (x coordinate)
        frogRow = frog.y - 25,
        frogCollided = frog.x + frog.width,
        itemCollided = item.x + frog.width;
      // Returns true if both the frog and inputted item are on the same y axis and their x axis is overlapping each other to signify collision.
      // Will check whether frog is touching the left or right side of the item or is within the item. If any of the before is true, return true.
      // Will return false if there are no collisions.
      return ((frogRow === item.y) ? ((item.x < frogCollided && frog.x < item.x) || (itemCollided + item.width > frog.x && frogCollided > itemCollided)) : false);
    },

    // Detects whether the frog is on a floating item in the water. True if on the item.
    onFloatingObj = (frog: MapItems, item: MapItems, newPosition: number = 0): boolean => {
      // If no newPosition inputted (default 0)
      if (newPosition === 0) {
        const frogRow = frog.y - 25;
        // Returns true if the frog's x axis is within the floating item x axis and width and is on the same axis as that item row.
        return ((frog.x >= item.x && frog.x < item.x + item.width) && (frogRow === item.y));
      }
      // Else if newPosition is provided, use that instead to find whether the frog is on floating object at that position.
      else {
        // Returns true if the frog's x axis is within the floating item x axis and width and is on the same axis as that item row.
        return ((frog.x >= item.x && frog.x < item.x + item.width) && (newPosition === item.y));
      };
    },

    // Function that returns an array filled with all the floating objects the frog is currently standing on.
    frogOnFloatingObj = (s: State, newPosition: number = 0): ReadonlyArray<MapItems> => {
      // If no newPosition inputted (default 0)
      if (newPosition === 0) {
        const
          // Checks if frog is currently colliding with planks or a crocodile to determine if it's on it or not.
          frogOnPlanks = s.planks.filter((plank) => onFloatingObj(s.frog, plank)),
          frogOnCrocs = s.crocodiles.filter((crocodile) => onFloatingObj(s.frog, crocodile));
        // Will return an array filled with obstacles that frog is currently standing on.
        return (frogOnPlanks.concat(frogOnCrocs));
      }
      // Else if a newPosition is provided, use that instead to find if the frog is currently standing on a specific crocodile
      else {
        return (s.crocodiles.filter((crocodile) => onFloatingObj(s.frog, crocodile, newPosition - 25)));
      };
    },
    
    // Function that detect any collisions by the frog constantly.
    detectCollisions = (s: State): State => {
      const
        frogRow = s.frog.y - 25,
        // Loops through obstacles MapItems array and detect collision for each obstacle. New array of obstacles collided will 
        // be returned if there are collisions. Checks for the new array length to determine if it's empty or not.
        // If the new array has an object inside it (length more than 0), collision has occured.
        hasCollidedCar = s.cars.filter((car) => mapItemCollision(s.frog, car)).length > 0,
        // Checks whether the frog is on the water.
        isInWater = (frogRow >= 50 ? frogRow <= 250 : false),
        // Checks whether frog is on a floating object in the water
        objFrogIsOn = frogOnFloatingObj(s),
        isFrogFloating = objFrogIsOn.length > 0,    // Will return true if frog is currently on a floating object
        // Check if the time elapsed since saved time is more than 2 seconds and the frog is still standing on the same crocodile object. If so, kill the frog.
        crocAttacks = s.frog.timeStepped.length > 0 ? ((s.time >= Number(s.frog.timeStepped[0]) + 200) && (objFrogIsOn[0].id === s.frog.timeStepped[1])) : false;

      // Return the state with isDead being true if the frog has collided on a car or is in water without being on a floating object.
      // If frog has no collisions and is either not in the water or on a floating object, return false.
      return ({
        ...s,
        isDead: (hasCollidedCar) || (isFrogFloating && crocAttacks) || (isInWater && !isFrogFloating)
      });
    },

    // A function to update the round the game is in the state. Round will only be 0, 1, 2 or 3
    updateRound = (s: State): number => {
      // If current round is 3 (next round being 4), reset the round back to 0
      if (s.round >= 3) {
        return s.round - 3;
      }
      // Else if current round is 0, 1 or 2. Increment the current round and return it
      else {
        return s.round + 1;
      };
    },

    // Checks whether the row the frog has visited is unique (first time moved there) or not.
    toUpdate = (s: State, hasMoved: boolean, index: number): boolean => {
      // Will return true if visited row is unique. False otherwise.
      return (hasMoved ? (!s.uniqueRows[index] ? true : false) : false);
    },

    // Function that updates the score every game tick. Will increase by 5 for each unique row moved and increase by 100
    // if frog fills an unfilled target. Filled targets will have no score gain.
    updateScore = (s: State): number => {
      // Gets the number of the target the frog current is in.
      const targetNum = getTargetNumber(s);
      // If frog at target and target has not been filled, add 100 score.
      if (s.frog.y === 25 && isTarget(s.frog) && !s.targetsFilled.includes(targetNum[0])) {
        return (s.score + 100);
      }
      // If frog is at target and target has already been filled, do not add any score.
      else if (s.frog.y === 25 && isTarget(s.frog) && s.targetsFilled.includes(targetNum[0])) {
        return s.score;
      }
      // If frog is eating the fly, grant 50 points.
      else if (s.frog.x === s.fly.x && s.frog.y === s.fly.y) {
        return s.score + 50;
      }
      // Else checks if frog is at a unique row to add 5 score if it is.
      else {
        const
          // Gets the array of booleans indicating the rows the frog has moved. True if it has, false if it hasn't
          rowsMoved = s.frog.rowsMoved,
          // Replaces all true values with 5 and all false values with 0. Will only replace all true values that are unique
          // (not visited before). If the row is not unique (frog has visited it before), the value will be replaced with 0.
          score = rowsMoved.map((hasMoved, i) => (toUpdate(s, hasMoved, i) ? Number(5) : Number(0))),
          // Reduce the score array to find total score gained. Each unique row moved is +5 score.
          totalScore = score.reduce((initialValue, accumulator) => (initialValue + accumulator));
        // Will add the new score gain from unique rows moved to the current score.
        return (s.score + totalScore);
      };
    },

    // Function that updates the high score every game tick if the current score is higher or equal than the high score.
    updateHighScore = (s: State): number => {
      // Returns the score if the score is higher or equal than the highscore. Else just return the highscore number.
      return (s.score >= s.highScore ? s.score : s.highScore);
    },

    updateFly = (s: State): MapItems => {
      // If frog is directly on top of the fly, move the fly off the map after that to "hide" it
      if (s.frog.x === s.fly.x && s.frog.y === s.fly.y) {
        return ({
          ...s.fly,
          x: -200
        });
      }
      // Else if frog not on the fly, return the fly object back so that it's position remained stationary.
      else {
        return s.fly;
      };
    },

    // Function that returns the target number based on current x position.
    getTargetNumber = (s: State): ReadonlyArray<number> => {
      // Checks if frog x is in a target x. If it is, replace the target x coordinate with the target number and then filter out
      // values that are <= 5. The returned array will only contained 1 target number ranging from 0 - 5.
      return (GAME_CONSTANTS.TARGET_POS
        .map((pos, i) => (s.frog.y + 25 === 50 ? (s.frog.x - 25 === pos ? i : pos) : pos))
        .filter((target) => (target <= 5)));
    },

    // Function to check whether the target already exists in the targetArray. Will return an array with the target if unique,
    // or an empty array if not unique.
    checkDuplicates = (targetArray: ReadonlyArray<number>, target: number): ReadonlyArray<number> => {
      // True if the target already exists in the targetArray. False if doesn't.
      const hasDuplicate = targetArray.filter((num) => (target === num)).length > 0;
      // If target is unique, return an array with the target number in it. Else return an empty array.
      return (hasDuplicate ? [] : [target]);
    }, 

    // Function that update the number array of targets to indicate which targets are filled by a frog.
    // Will concat the target number that is filled by a frog
    updateTargets = (s: State): ReadonlyArray<number> => {
      // Will create a new array of all target numbers filled with a frog.
      const 
        // Gets the target number of any target the frog is in if it is.
        newTarget =  getTargetNumber(s),
        // Will check if the target has already been filled by checking for duplicates in the original target array.
        // If so, concat an empty array. Else, concat the target number to the original array and assign it to a new totalTargets array
        totalTargets = s.targetsFilled.concat(checkDuplicates(s.targetsFilled, newTarget[0]));
      // Return array filled with every target number that has been filled with a frog.
      return totalTargets;
    },
    
    // Function to update the speed of input array by adding all of them with the speedIncrease input
    updateSpeed = (speedArray: ReadonlyArray<number>, speedIncrease: number): ReadonlyArray<number> => {
      // Creates a new array with the speed + speedIncrease to update the obstacles' new speed.
      // If speed is negative (moving left), subtract with the speedIncrease to increase speed.
      // Else if speed is positive (moving right), add with the speedIncrease to increase speed.
      return (speedArray.map((speed) => (speed > 0 ? speed + speedIncrease : speed - speedIncrease)));
    },

    // Function that resets the state of the game after frog dies to give the illusion of a new round
    resetState = (s: State): State => {
      // Will only reset the game if game has already started. Else just return original state and don't do anything.
      // Will reset every object property besides time and high score to initial state. 
      // Round is incremented by 1 to show a new round has started
      return (s.gameStart ? {
        ...s,
        round: updateRound(s),
        score: 0,
        frog: createFrog(),
        cars: createAllCars([], GAME_CONSTANTS.CAR_SPEED),
        planks: createAllPlanks([], GAME_CONSTANTS.PLANK_SPEED),
        crocodiles: createAllCrocs([], GAME_CONSTANTS.CROC_SPEED),
        fly: createAllFlies(updateRound(s)),
        targetsFilled: [],    
        isDead: false,
        uniqueRows: [false, false, false, false, false, false, false, false, false],
        carSpeed: GAME_CONSTANTS.CAR_SPEED,
        plankSpeed: GAME_CONSTANTS.PLANK_SPEED,
        crocSpeed: GAME_CONSTANTS.CROC_SPEED
      }
      : s);
    },

    // Function to change the gameStart property of the state of the game to signify the game has started and to enter the map.
    startGame = (s: State, toStart: boolean): State => {
      return ({
        ...s,
        gameStart: toStart
      });
    },

    // Tick function that changes the game's state based on how much time elapsed and moves the mapitems each tick.
    // Adapted from Tim's asteroids to suit Frogger needs
    tick = (s: State, elapsed: number): State => {
      // If game hasn't started yet, only changed the time elapsed. Do not change anything else so that game will be "frozen"
      if (!s.gameStart) {
        return ({
          ...s,
          time: elapsed
        });
      }
      // Updates the state of the game per tick by calling all the update functions.
      // If not all targets are filled yet, update game state as normal
      else if (!s.isDead && s.targetsFilled.length <= 6) {
        // Call detectCollisions which will detect whether frog is dead or not due to colliding with an obstacle.
        return detectCollisions({
          ...s,
          time: elapsed,
          score: updateScore(s),                        // Call updaeScore to update score
          highScore: updateHighScore(s),                // Updates the High Score based on current score
          frog: moveMapItems(s, s.frog),                // Moves the frog
          cars: moveObstacles(s, s.cars),               // Move all cars simulataneously  every tick
          planks: moveObstacles(s, s.planks),           // Move all planks every tick
          crocodiles: moveObstacles(s, s.crocodiles),   // Move all crocodiles  every tick
          fly: updateFly(s),                            // Update fly if the frog "ate" it
          targetsFilled: updateTargets(s),              // Update the array if target is filled by a frog.
          uniqueRows: s.frog.rowsMoved                  // Update uniqueRows if frog visited.
        });
      }
      // If all targets are filled, reset the targetsFilled and update obstacles difficulty by increasing their speed.
      // Increase score and high score by 200 as bonus points for filling all the targets
      else if (!s.isDead && s.targetsFilled.length > 6) {
        return detectCollisions({
          ...s,
          time: elapsed,
          round: updateRound(s),                            // Update the round based on the current round
          score: updateScore(s) + 200,                      // Call updaeScore to update score
          highScore: updateHighScore(s) + 200,              // Updates the High Score based on current score
          frog: moveMapItems(s, s.frog),                    // Moves the frog
          cars: createAllCars([], s.carSpeed, 0.2),         // Recreate all car objects but with +0.2 faster speed for increase in difficulty
          planks: createAllPlanks([], s.plankSpeed, 0.2),   // Recreate all plank objects but with +0.2 faster speed for increase in difficulty
          crocodiles: createAllCrocs([], s.crocSpeed, 0.2), // Recreate all crocodiles objects but with +0.2 faster speed for increase in difficulty
          fly: createAllFlies(updateRound(s)),              // Recreate the fly at the new position once a new round starts.
          targetsFilled: [],                                // Empty the targetsFilled array.
          uniqueRows: s.frog.rowsMoved,                     // Update uniqueRows if frog visited.
          carSpeed: updateSpeed(s.carSpeed, 0.2),           // Increment all speeds of cars by 0.2
          plankSpeed: updateSpeed(s.plankSpeed, 0.2),       // Increment all speeds of planks by 0.2
          crocSpeed: updateSpeed(s.crocSpeed, 0.2)          // Increment all speeds of crocodiles by 0.2
        });
      }
      // Else if the frog is dead, freeze the game by returning the state back until the game is restarted (pressing R key)
      else {
        return s;
      };
    },

    // Reduce the state of the whole game. If event is Move, move the frog based on keyboard input. 
    // If it's Tick, call the tick function to move map items and update the game state every game tick (10ms).
    // Else if its Reset, reset the state of the game to start a new round.
    // This function is adapted from Tim's asteroid code to fit Frogger.
    reduceState = (s: State, event: Tick | Move | Reset | Start): State => {
      // If game hasn't started yet, do not move the frog or allow key inputs.
      if (!s.gameStart) {
        // Moves the frog based on the axis and direction of the Move class.
        return (event instanceof Move ? s :
        // If reset (Key R being pressed), reset the game.
        (event instanceof Reset ? resetState(s) :
        // Else if the mouse clicked the start button, start the game
        (event instanceof Start ? startGame(s, event.toStart) :
        // Else call tick function to initiate a game tick.
        tick(s, event.elapsed))
        ));
      }
      // Else if game have started, move the frog.
      else {
        // Moves the frog based on the axis and direction of the Move class.
        return (event instanceof Move ? {
          ...s,
          frog: {
            ...s.frog,
            vel: GAME_CONSTANTS.FROG_STEP * event.direction,
            axis: event.axis
          }
        } :
        // If reset (Key R being pressed), reset the game.
        (event instanceof Reset ? resetState(s) :
        // Else if the mouse clicked the start button, start the game
        (event instanceof Start ? startGame(s, event.toStart) :
        // Else call tick function to initiate a game tick.
        tick(s, event.elapsed))
        ));
      }
    },

    // Every 10ms, emit an observable to imitate a clock
    clock$ = interval(10)
      .pipe(map(elapsed => new Tick(elapsed))),

    // Key movements for Frogger using observables. Filter out code that was pressed and mapped them to result. 
    // So when a key is pressed the result function is called with the keys pressed as the input.
    key$ = <T>(k: Key, result: () => T) => 
    fromEvent<KeyboardEvent>(document, "keypress")
      .pipe(
        filter(({code}) => code === k),
        filter(({repeat}) => !repeat),
        map(result)),
    
    // Listen to mouse events. If mouse click event occurs and it's the left mouse button (button 0), and the mouse cursor
    // coordinates are over the start button, call the result function with the button pressed as the input.
    mouse$ = <T>(result: () => T) =>
    fromEvent<MouseEvent>(document, "click")
    .pipe(
      filter(({button}) => button === 0),
      filter(({x}) => x >= 200 && x <= 400),
      filter(({y}) => y >= 375 && y <= 425),
      map(result)),
    
    // Move based on key pressed. Will create a Move or Reset class with parameters based on the key pressed.
    // Move class will have their direction (-1 being left and up and +1 being down and right) and their axis being moved.
    // Reset class will have true to indicate a Reset.
    leftMovement$ = key$("KeyA", () => new Move(-1, "x")),
    rightMovement$ = key$("KeyD", () => new Move(1, "x")),
    upMovement$ = key$("KeyW", () => new Move(-1, "y")),
    downMovement$ = key$("KeyS", () => new Move(1, "y")),
    // Keyboard key to reset the game/round
    reset$ = key$("KeyR", () => new Reset(true)),
    // If mouse is clicked over the start button, create a class Start to trigger the start of the game.
    click$ = mouse$(() => new Start(true)),
    
    // Merge all the input observables and clock together.
    // Will then execute reduceState function with the accumulate being the initialState at first. Then the accumulator will be
    // the current game state.
    // Finally, subscribe them to the updateView to update view of the whole game. (Will modify SVG elements)
    keyboardAndMouse$ =
      merge(leftMovement$, rightMovement$, upMovement$, downMovement$, reset$, click$),
    // Merges twice as merge can only accept maximum 6 observable streams.
    clockAndInputs$ =
      merge(keyboardAndMouse$, clock$)
      .pipe(
        scan(reduceState, initialState))
      .subscribe(updateView);
    
  // A function that updates the view of all map items in the game. Has side effects due to mutating SVG and HTML attributes.
  // All SVG elements are mutated and updated here, leading to this only function being impure and have side effects.
  // Based on Tim's asteroids to suit Frogger needs but is modified heavily.
  function updateView(s: State): void {
    const 
      svg = document.querySelector("#svgCanvas") as SVGElement & HTMLElement,

      // Function to create an SVG element based on its input given.
      // Since mutates the HTML element, it is impure. Is declared locally within the updateView so the side effects are isolated.
      createSvgElement = (elem: string) => (id: string, x: number, y: number, colour: string = "", stroke: string = "", width: number = 0, height: number = 0, r: number = 0): Element => {
        // Gets the element if it exist, else create it if null.
        const element = document.getElementById(id) || document.createElementNS(svg.namespaceURI, elem);
        element.setAttribute("id", id);
        // If elem is not a circle, set x attribute using label x. Else if a circle, set using x using cx label
        elem !== "circle" ? element.setAttribute("x" , String(x)) : element.setAttribute("cx" , String(x));
        elem !== "circle" ? element.setAttribute("y" , String(y)) : element.setAttribute("cy" , String(y));
        
        // If creating shapes
        if (elem !== "text") {
          // Stroke width will be 1 if other shapes, 2 if frog.
          const strokeWidth = id !== "frog" ? "1" : "2";
          // If the colour  and stroke are not default value (empty string) set the colour.
          // Else don't set any colour or stroke for the element
          (colour.length > 0) && (stroke.length > 0) ? element.setAttribute(
            "style",
            "fill: " + colour + "; stroke: " + stroke + "; stroke-width: " + strokeWidth + ";"
          ) : 0;
          // If width and height are not default value of 0 (included in the caller arguements), set it. Else don't
          width !== 0 ? element.setAttribute("width", String(width)) : 0;
          height !== 0 ? element.setAttribute("height", String(height)) : 0;
          // If frog, set the radius of frog to r. Else if not frog, do not do anything.
          elem === "circle" ? element.setAttribute("r", String(r)) : 0;
          // Add element to SVG view and returns it.
          svg.appendChild(element);
          return element;
        }
        // Else if creating text element.
        else {
          return createText(element, id);
        };
      },
      
      // Curries the above function but with the shape included.
      createSvgRect = createSvgElement("rect"),
      createSvgCircle = createSvgElement("circle"),
      createSvgText = createSvgElement("text"),

      // Function to create text based on their ID.
      createText = (element: Element, id: string): Element => {
        // If creating the score display
        if (id === "score") {
          element.textContent = "Score: ";
          element.setAttribute(
            "style",
            "font-size: 20px; fill: white;"
          );
        }
        // Else if creating the high score display
        else if (id === "highScore") {
          element.textContent = "High Score: ";
          element.setAttribute(
            "style",
            "font-size: 20px; fill: white;"
          );
        }
        // Else if creating the game over display.
        else if (id === "gameOver") {
          element.textContent = "GAME OVER! Press R to Restart the Game";
          element.setAttribute(
            "style",
            "font-size: 28px; font-family: sans-serif; fill: red; font-weight: bold;"
          );
          // Set visibiltiy to hidden so it's invisible at game start and only will be visible if frogger dies.
          element.setAttribute("visibiliy", "hidden");
        }
        // Else if creating the start button text.
        else if (id === "startText") {
          element.textContent = "Start Game";
          element.setAttribute(
            "style",
            "font-size: 30px; fill: white; font-weight: Bold"
          );
        }
        // Else if it's the main menu display text for the game's name.
        else if (id === "mainText") {
          element.textContent = "FROGGER";
          element.setAttribute(
            "style",
            "font-size: 100px; fill: white; font-weight: Bold;"
          );
        }
        svg.appendChild(element);
        return element;
      };
    
    // Function to create all map items. As it sets attributes, this function is impure. However it is only called within
    // updateView hence side effects is isolated within updateView function.
    function createMap(): void {
      // Add river region. WIll be a rectangle that is blue in colour with width 600 and height 250.
      const river = createSvgRect("river", 0, 50, "blue", "blue", 600, 250);

      // Added Spawn Zone. Will be a purple rectangle with width 600 and height 50
      const spawnZone = createSvgRect("spawn", 0, 550, "purple", "purple", 600, 50);
      
      // Add the safe zone between ground and river. Same look as the spawn zone.
      const safeZone = createSvgRect("safe", 0, 300, "purple", "purple", 600, 50);

      // End end zone where targets are. Same look as the spawn zone.
      const endZone = createSvgRect("end", 0, 0, "purple", "purple", 600, 50);
      
      // Function that creates the Target SVG shapes at the input x coordinates
      function createTargets(x: number): void {
        // Creates each target as a square with their x coordinates based on their input and their id being target + x.
        // For example 2 will create an id of target2.
        const target = createSvgRect("target" + String(x), x, 0, "blue", "blue", 50, 50);
      };

      // Loop through all the x coordinates and create targets at those said coorrdinates. Will curry the target number.
      GAME_CONSTANTS.TARGET_POS.forEach(createTargets);
    };

    // Function to create all main menu SVG items.
    function createMainMenu(visibility: string): void {
      const 
        // Create a black screen that overlays over the whole game if it hasn't beem created yet.
        mainScreen = document.getElementById("mainScreen") || createSvgRect("mainScreen", 0, 0, "black", "black", 600, 600),
      
        // Create a start button and start textin the main menu if it hasn't been created yet.
        startButton = document.getElementById("startButton") || createSvgRect("startButton", 200, 300, "crimson", "black", 200, 50),
        startText = document.getElementById("startText") || createSvgText("startText", 218, 333),

        mainMenuText = document.getElementById("mainText") || createSvgText("mainText", 50, 200);
      
      // Change visiblity of all main menu svg items based on input. If game has started, hide them.
      mainScreen.setAttribute("visibility", visibility);
      startButton.setAttribute("visibility", visibility);
      startText.setAttribute("visibility", visibility);
      mainMenuText.setAttribute("visibility", visibility);
    };

    // Function to add the fly interactable to the map.
    // Default values for x and y will be the first x and y coordinate in the game constants
    // Similarly to the createMapItems functions, it is impure but is only called within the updateView function.
    function createFlySVG(x: number = GAME_CONSTANTS.FLY_X_SPAWN[0], y: number = GAME_CONSTANTS.FLY_Y_SPAWN[0]): Element {
      // Add fly interactable in the game. Will only add 1 per round/game.
      return createSvgCircle("fly", x, y, "orange", "black", 0, 0, 15);
    };

    // Function to add frogger last so that it overlays over every other SVG elements in the game.
    // Default values of x and y are 300 and 575 respectively. The default id will be just "frog".
    // Similarly to the createMapItems functions, it is impure but is only called within the updateView function.
    function createFrogSvg(x: number = 300, y: number = 575, str: string = ""): Element {
      // Add Frogger in game. Will pass frog + str into id parameter. Hence if str is 2, id will be frog2
      return createSvgCircle("frog" + str, x, y, "green", "black", 0, 0, 20);
    };

    // Function to create current score at the bottom left of the screen.
    // Same as in createFrogSVG. Impure function but is only called by the updateView function.
    function createScore(): Element {
      // Add score to the top left of the screen. Will create a text element for the score.
      return createSvgText("score", 15, 580);
    };

    // Function to create current score at the bottom left of the screen.
    // Same as the createScore function. Despite being impure, it is only called within the updateView function.
    function createHighScore(): Element {
      // Add score to the top left of the screen
      return createSvgText("highScore", 425, 580);
    };

    // Function to create the Game Over text. Visiblity is set to hidden so it will only be visible once a Game Over has occur.
    // Also an impure function but is only called in the updateView function.
    function createGameOver(): Element {
      // Creates Game over text but will be invisible initially. Only visible once frogger dies.
      return createSvgText("gameOver", 20, 335);
    };

    // If at game start, create all the MapItems and the whole map. Also creates the main menu screen.
    if (s.time === 0) {
      createMap();
    };
    const 
      // Function to update all obstacles SVG with their x and y coordinates.
      updateObstacles = (item: MapItems): void => {
        // Creates the SVG obstacle based on their properties and return the Element once created.
        // This is a function to create the obstacles in the game if they do not exist yet.
        function createObstaclesSvg(item: MapItems): Element {
          // Will create an obstacles based on their MapItems object properties.
          return createSvgRect(String(item.id), item.x, item.y, item.colour, "black", item.width, item.height);
        };

        // Attempts to get the elem by ID. If null is found, create the SVG item on the map
        const elem = document.getElementById(item.id) || createObstaclesSvg(item);
          
        // Sets the x coordinate of the obstacle to their new position to imitate movement.
        elem.setAttribute("x", String(item.x));
      },

      // Function to create all target frogs SVG elements
      createTargetFrogs = (targetNum: number): void => {
        // Gets the x coordinate for each target.
        const 
          targetX = GAME_CONSTANTS.TARGET_POS[targetNum],
          // Create SVG Frog images for each target if it hasn't been created yet.
          frogTarget = document.getElementById("frog" + String(targetNum)) || createFrogSvg(targetX + 25, 25, String(targetNum));
      },

      // Will change the visibiltiy of the frog targets based on input. Will be invisible at game start and only becomes visible
      // once a target is filled by the frog.
      changeVisibility = (targetNum: number, visibility: string): void => {
        const frogTarget = document.getElementById("frog" + String(targetNum))!;
        frogTarget.setAttribute("visibility", visibility);
      },

      // Will fill the target slots with an SVG image of the  frog.
      fillTargets = (targetNum: number): void => {
        // Will only create a frog SVG image if at least one single target is filled. Won't create any if no target is filled.
        if (targetNum >= 0) {
          changeVisibility(targetNum, "visible");
        }
        // Else if no target is filled or if every target is filled, change all the frog at target visibility to hidden.
        else {
          const targets = [0, 1, 2, 3, 4, 5];
          targets.forEach((target) => (changeVisibility(target, "hidden")));
        };
      };

    // Passes each obstacle in the arrays into updateObstacles to update their positions on the map every game tick.
    s.cars.forEach(updateObstacles);
    s.planks.forEach(updateObstacles);
    s.crocodiles.forEach(updateObstacles);

    // Creates the fly SVG element if it doesn't already exist. Else just get the element and assign it to variable fly.
    const fly = document.getElementById("fly") || createFlySVG();
    
    // Sets the x and y coordinates of the frog every tick or keyboard movement.
    fly.setAttribute("cx", String(s.fly.x));
    fly.setAttribute("cy", String(s.fly.y));

    // Creates the frog SVG element if it doesn't already exist. Else just get the element and assign it to variable frog.
    const frog = document.getElementById("frog") || createFrogSvg();
    
    // Sets the x and y coordinates of the frog every tick or keyboard movement.
    frog.setAttribute("cx", String(s.frog.x));
    frog.setAttribute("cy", String(s.frog.y));

    // Create the frog SVG images at the target if they do not exist yet at game start.
    GAME_CONSTANTS.TARGETS.forEach(createTargetFrogs);

    // Fill targets with SVG images of frogs once filled.
    s.targetsFilled.forEach((target) => (fillTargets(target)));

    // Creates the score and high score SVG text element if it doesn't exist. Else just get the elements
    const 
      score = document.getElementById("score") || createScore(),
      highScore = document.getElementById("highScore") || createHighScore(),
      // Create GameOver SVG Text if doesn't exist, else just get the element.
      gameOver = document.getElementById("gameOver") || createGameOver();

    // Sets the Text content to be current score and highscore in game's state
    score.textContent = "Score: " + String(s.score);
    highScore.textContent = "High Score: " + String(s.highScore);

    // If game hasn't started yet, create the main menu.
    if (!s.gameStart) {
      createMainMenu("visible");
    }
    // Else if game has started, set visibility of all main menu items to false.
    else {
      createMainMenu("hidden");
    };

    // If dead, reveal GameOver SVG text by making it visible.
    if (s.isDead) {
      gameOver.setAttribute("visibility", "visible");
    }
    // Else if still alive, set visibility of text to hidden.
    else {
      gameOver.setAttribute("visibility", "hidden");
    };
  };

  // If key is pressed, highlight the key in the HTML table.
  // Will also highlight the start button if the mouse is hovering over it.
  // Function is impure but is only called within the subscribe call hence the side effects are isolated.
  function showHighlights(): void {
    // Function to listen for key events and will highlight the keys in the HTML table if pressed.
    // Will also change the colour of the start button if the mouse cursor is hovering over it.
    function showHighlight(k: Key): void {
      const 
        // Get control HTML elements
        arrowKey = document.getElementById(k)!,
        // Get startbutton SVG
        startButton = document.getElementById("startButton")!,

        // Get keyboard presses
        keys$ = (e: Event) => fromEvent<KeyboardEvent>(document, e)
          .pipe(
            filter(({code}) => code === k)),
        
        // Get mouse movement if within the start button box
        mouseOnStart$ = (e: Event) => fromEvent<MouseEvent>(document, e)
          .pipe(
            filter(({x, y}) => (x >= 200 && x <= 400) && (y >= 375 && y <= 425))),
        
        // Get mouse movement if outside the start button box
        mouseNotOnStart$ = (e: Event) => fromEvent<MouseEvent>(document, e)
          .pipe(
            filter(({x, y}) => (x < 200 || x > 400) || (y < 375 || y > 425)));

      // Subscribe the key observables and add a highlight element to them if keydown. If keyup, remove the highlight.
      keys$("keydown").subscribe(_ => arrowKey.classList.add("highlight"));
      keys$("keyup").subscribe(_ => arrowKey.classList.remove("highlight"));
      
      // Subscribe the mouse movement observable and then change the startButton colour
      // Changes colour if cursor is on the start button. Reverts colour when not.
      mouseOnStart$("mousemove").subscribe(_ => startButton.setAttribute("style", "fill: DarkRed;"));
      mouseNotOnStart$("mousemove").subscribe(_ => startButton.setAttribute("style", "fill: crimson;"));
    }
    // Will call showKey with all possible key inputs. (WASD and R and mouse movement)
    showHighlight("KeyW");
    showHighlight("KeyA");
    showHighlight("KeyS");
    showHighlight("KeyD");
    showHighlight("KeyR");
    showHighlight("mousemove");
  };

  // Every 10ms will subscribe the showKeys to display the keys highlight when pressed.
  timer(10).subscribe(showHighlights);
};

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  };
};