import {ChangeDetectorRef, Component} from '@angular/core';
import {Node, Link} from './d3';
import {ViewChild, TemplateRef} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  @ViewChild('alertDialog') alertDialog: TemplateRef<any>;

  constructor(private changeDetector: ChangeDetectorRef, private dialog: MatDialog) {
  }

  url = 'http://SocialNetworkAnalysis-env.eba-mtjbwbch.eu-central-1.elasticbeanstalk.com/social-analysis/prediction';
  urlDemo = this.url + '/demo';

  isInputtingData = false;
  isShowingTable = false;
  isShowingGraphs = false;

  numberOfUsers;
  codeNames;

  predictedGraphs: [{}, {}, {}, {}, {}];

  mainNodes: Node[] = [];
  mainLinks: Link[] = [];

  private commonFriendsNodes: Node[] = [];
  private commonFriendsLinks: Link[] = [];

  private zhakkarNodes: Node[] = [];
  private zhakkarLinks: Link[] = [];

  private adamicAdarNodes: Node[] = [];
  private adamicAdarLinks: Link[] = [];

  private minPathDijkstraNodes: Node[] = [];
  private minPathDijkstraLinks: Link[] = [];

  private hitProbabilityNodes: Node[] = [];
  private hitProbabilityLinks: Link[] = [];

  openAlertDialog() {
    this.dialog.closeAll();
    this.dialog.open(this.alertDialog);
  }

  generateArrayOfNumbers() {
    let codeName = "@";

    return Array(this.numberOfUsers).fill("").map(() => {
      codeName = AppComponent.generateCodeName(codeName);
      return codeName;
    });
  }

  demo() {
    this.isInputtingData = false;
    this.isShowingTable = false;
    this.isShowingGraphs = false;
    this.dialog.closeAll();
    this.resetGraphs();

    this.getDemoMatrix().then(r =>
      this.demoPredictByCommonFriends().then(r =>
        this.demoPredictByZhakkar().then(r =>
          this.demoPredictByAdamicAdar().then(r =>
            this.demoPredictByMinPathDijkstra().then(r =>
              this.demoPredictByHitProbability().then(r => {
                this.generatePredictedGraphs();
                this.isShowingGraphs = true
              }))))));
  }

  changeInput() {
    this.isInputtingData = !this.isInputtingData;

    if (!this.isInputtingData) {
      this.isShowingTable = false;
      this.isShowingGraphs = false;
      this.dialog.closeAll();
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
    this.isShowingGraphs = false;
    this.changeDetector.detectChanges();
    this.resetGraphs();

    let cells = document.getElementsByClassName("matrix-cell");

    let relationsMatrix = [];
    let row = [];
    let counter = 0;
    for (let i = 0; i < cells.length; i++) {
      let n = cells.item(i).textContent;
      if (n === "") {
        row.push(null);
      } else {
        row.push(Number.parseInt(n));
      }

      counter++;

      if (counter === this.numberOfUsers) {
        counter = 0;
        relationsMatrix.push(row);
        row = [];
      }

    }

    if (AppComponent.validateMatrix(relationsMatrix)) {
      this.pushMatrixToGraph(relationsMatrix, 'main');

      this.predictByCommonFriends(relationsMatrix).then(r =>
        this.predictByZhakkar(relationsMatrix).then(r =>
          this.predictByAdamicAdar(relationsMatrix).then(r =>
            this.predictByMinPathDijkstra(relationsMatrix).then(r =>
              this.predictByHitProbability(relationsMatrix).then(r => {
                this.generatePredictedGraphs();
                this.isShowingGraphs = true
              })))));
    } else {
      this.openAlertDialog();
    }
  }

  private resetGraphs() {
    this.mainNodes = [];
    this.mainLinks = [];
    this.commonFriendsNodes = [];
    this.commonFriendsLinks = [];
    this.zhakkarNodes = [];
    this.zhakkarLinks = [];
    this.adamicAdarNodes = [];
    this.adamicAdarLinks = [];
    this.minPathDijkstraNodes = [];
    this.minPathDijkstraLinks = [];
    this.hitProbabilityLinks = [];
    this.hitProbabilityNodes = [];

    this.predictedGraphs = [{}, {}, {}, {}, {}];
  }

  private generatePredictedGraphs() {
    this.predictedGraphs = [
      {"name":"Метод спільних сусідів",
        "nodes": this.commonFriendsNodes,
        "links": this.commonFriendsLinks
      },
      {
        "name":"Коефіцієнт Жаккара",
        "nodes": this.zhakkarNodes,
        "links": this.zhakkarLinks
      },
      {
        "name":"Коефіцієнт Адамік-Адара",
        "nodes": this.adamicAdarNodes,
        "links": this.adamicAdarLinks
      },
      {
        "name":"Аналіз досяжності: пошук мінімального шляху",
        "nodes": this.minPathDijkstraNodes,
        "links": this.minPathDijkstraLinks
      },
      {"name":"Аналіз досяжності: вирогідність зв'язку",
        "nodes": this.hitProbabilityNodes,
        "links": this.hitProbabilityLinks
      }
    ];
  }

  private pushMatrixToGraph(relationsMatrix, graphMode) {
    console.log(relationsMatrix)
    let codeName = "@";
    let nodes: Node[] = [];
    for (let i = 0; i < relationsMatrix.length; i++) {
      codeName = AppComponent.generateCodeName(codeName);
      nodes.push(new Node(i, codeName));
    }

    let links: Link[] = [];
    for (let i = 0; i < relationsMatrix.length; i++) {
      for (let j = 0; j < relationsMatrix[i].length; j++) {
        if (relationsMatrix[i][j] === 1) {
          links.push(new Link(i, j));
        }
      }
    }

    switch (graphMode) {
      case 'main':
        this.mainNodes = nodes;
        this.mainLinks = links;
        break;
      case 'common-friends':
        this.commonFriendsNodes = nodes;
        this.commonFriendsLinks = links;
        break;
      case 'zhakkar':
        this.zhakkarNodes = nodes;
        this.zhakkarLinks = links;
        break;
      case 'adamic-adar':
        this.adamicAdarNodes = nodes;
        this.adamicAdarLinks = links;
        break;
      case 'min-path-dijkstra':
        this.minPathDijkstraNodes = nodes;
        this.minPathDijkstraLinks = links;
        break;
      case 'hit-probability':
        this.hitProbabilityNodes = nodes;
        this.hitProbabilityLinks = links;
        break;
    }
  }

  private async makePredictRequest(methodUrl, relationsMatrix) {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    let response = await fetch(this.url + methodUrl, {
      method: "POST",
      headers: myHeaders,
      mode: 'cors',
      body: JSON.stringify(relationsMatrix)
    });

    return await response.json();
  }

  private async makeDemoPredictRequest(methodUrl) {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    let response = await fetch(this.urlDemo + methodUrl, {
      method: "GET",
      headers: myHeaders,
      mode: 'cors',
    });

    return await response.json();
  }

  private async predictByCommonFriends(relationsMatrix) {
    let methodUrl = '/common-friends';
    let predictedMatrix = await this.makePredictRequest(methodUrl, relationsMatrix);
    this.pushMatrixToGraph(predictedMatrix, 'common-friends');
  }

  private async predictByZhakkar(relationsMatrix) {
    let methodUrl = '/zhakkar';
    let predictedMatrix = await this.makePredictRequest(methodUrl, relationsMatrix);
    this.pushMatrixToGraph(predictedMatrix, 'zhakkar');
  }

  private async predictByAdamicAdar(relationsMatrix) {
    let methodUrl = '/adamic-adar';
    let predictedMatrix = await this.makePredictRequest(methodUrl, relationsMatrix);
    this.pushMatrixToGraph(predictedMatrix, 'adamic-adar');
  }

  private async predictByMinPathDijkstra(relationsMatrix) {
    let methodUrl = '/min-path-dijkstra';
    let predictedMatrix = await this.makePredictRequest(methodUrl, relationsMatrix);
    this.pushMatrixToGraph(predictedMatrix, 'min-path-dijkstra');
  }

  private async predictByHitProbability(relationsMatrix) {
    let methodUrl = '/hit-probability';
    let predictedMatrix = await this.makePredictRequest(methodUrl, relationsMatrix);
    this.pushMatrixToGraph(predictedMatrix, 'hit-probability');
  }

  private async getDemoMatrix() {
    let methodUrl = '/matrix';
    let mainMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(mainMatrix, 'main');
  }

  private async demoPredictByCommonFriends() {
    let methodUrl = '/common-friends';
    let predictedMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(predictedMatrix, 'common-friends');
  }

  private async demoPredictByZhakkar() {
    let methodUrl = '/zhakkar';
    let predictedMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(predictedMatrix, 'zhakkar');
  }

  private async demoPredictByAdamicAdar() {
    let methodUrl = '/adamic-adar';
    let predictedMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(predictedMatrix, 'adamic-adar');
  }

  private async demoPredictByMinPathDijkstra() {
    let methodUrl = '/min-path-dijkstra';
    let predictedMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(predictedMatrix, 'min-path-dijkstra');
  }

  private async demoPredictByHitProbability() {
    let methodUrl = '/hit-probability';
    let predictedMatrix = await this.makeDemoPredictRequest(methodUrl);
    this.pushMatrixToGraph(predictedMatrix, 'hit-probability');
  }

  private static validateMatrix(matrix: Array<Array<any>>): boolean {
    for (let i = 0; i < matrix.length; i++) {
      for (let j = 0; j < matrix[i].length; j++) {
        let element = matrix[i][j];
        if (element === null) {
          if (i !== j) {
            matrix[i][j] = 0;

            if (matrix[j][i] !== null && matrix[j][i] !== 0) {
              return false;
            }
          }

          continue;
        }

        if (!AppComponent.isNumber(element)) {
          return false;
        }

        let number = parseInt(element);
        if (number !== 0 && number !== 1) {
          return false;
        }

        if (matrix[i][j] !== matrix[j][i]) {
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
