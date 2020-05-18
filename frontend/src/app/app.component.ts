import {Component} from '@angular/core';
import APP_CONFIG from './app.config';
import {Node, Link} from './d3';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  nodes: Node[] = [];
  links: Link[] = [];

  isInputtingData = false;
  isShowingTable = false;
  numberOfUsers;
  codeNames;

  constructor() {
    const N = APP_CONFIG.N,
      getIndex = number => number - 1;

    /** constructing the nodes array */
    for (let i = 1; i <= N; i++) {
      this.nodes.push(new Node(i));
    }

    for (let i = 1; i <= N; i++) {
      for (let m = 2; i * m <= N; m++) {
        /** increasing connections toll on connecting nodes */
        this.nodes[getIndex(i)].linkCount++;
        this.nodes[getIndex(i * m)].linkCount++;

        /** connecting the nodes before starting the simulation */
        this.links.push(new Link(i, i * m));
      }
    }
  }

  generateArrayOfNumbers() {
    let codeName = "@";

    return Array(this.numberOfUsers).fill("").map(() => {
      codeName = AppComponent.generateCodeName(codeName);
      return codeName;
    });
  }

  changeInput() {
    this.isInputtingData = !this.isInputtingData;

    if (!this.isInputtingData) {
      this.isShowingTable = false;
    }
  }

  showTable() {
    let inputElement = document.getElementById("inputNumUsers");
    let inputValue = (<HTMLInputElement>inputElement).value;

    if (AppComponent.isNumberMoreThan2(inputValue)) {
      this.numberOfUsers = parseInt(inputValue);
      this.codeNames = this.generateArrayOfNumbers();

      this.isShowingTable = true;
    } else {
      let buttonElement = document.getElementById("showTable");

      inputElement.setAttribute("class", "form-control blinking");
      buttonElement.setAttribute("class", "btn btn-outline-secondary blinking");

      setTimeout(() => {
        inputElement.setAttribute("class", "form-control");
        buttonElement.setAttribute("class", "btn btn-outline-secondary");
      }, 2000);
    }
  }

  predictCommunications() {
    let cells = document.getElementsByClassName("matrix-cell");

    let relationsMatrix = [];
    let row = [];
    let counter = 0;
    for (let i = 0; i < cells.length; i++) {
      row.push(cells.item(i).textContent);
      counter++;

      if (counter === this.numberOfUsers) {
        counter = 0;
        relationsMatrix.push(row);
        row = [];
      }
    }

    if (AppComponent.validateMatrix(relationsMatrix)) {
      console.log(JSON.stringify({matrix: relationsMatrix}));
    }
  }

  private static validateMatrix(matrix: Array<Array<any>>): boolean {
    for (let i = 0; i < matrix.length; i++) {
      for (let j = 0; j < matrix[i].length; j++) {
        let element = matrix[i][j];
        if (element === "") {
          continue;
        }

        if (!AppComponent.isNumber(element)) {
          return false;
        }

        let number = parseInt(element);
        if (number !== 0 && number !== 1 && number !== -1) {
          return false;
        }
      }
    }

    return true;
  }


  private static isNumberMoreThan2(number: string): boolean {
    return AppComponent.isNumber(number) && parseInt(number) >= 3;
  }

  private static isNumber(number: string): boolean {
    return /^-?[\d.]+(?:e-?\d+)?$/.test(number);
  }

  private static generateCodeName(codeName) {
    if (codeName.endsWith("Z")) {
      codeName = AppComponent.incrementCodeNameLength(codeName);
    } else {
      codeName = AppComponent.incrementLastChar(codeName);
    }

    return codeName;
  }

  private static incrementCodeNameLength(codeName) {
    let length = codeName.length;
    let newCodeName = "";
    let notZIndex = -1;

    for (let i = length - 1; i >= 0; i--) {
      if (codeName.charAt(i) !== 'Z') {
        notZIndex = i;
        break;
      }
    }

    if (notZIndex !== -1) {
      newCodeName = codeName.substring(0, notZIndex);
      newCodeName += this.incrementChar(codeName.charAt(notZIndex));

      for (let i = notZIndex + 1; i < length; i++) {
        newCodeName += 'A';
      }
    } else {
      for (let i = 0; i < length + 1; i++) {
        newCodeName += "A";
      }
    }

    return newCodeName;
  }

  private static incrementLastChar(codeName) {
    let lastChar = codeName.charAt(codeName.length - 1);
    lastChar = this.incrementChar(lastChar);

    return codeName.substring(0, codeName.length - 1) + lastChar;
  }

  private static incrementChar(char) {
    return String.fromCharCode(char.charCodeAt(0) + 1);
  }

}
