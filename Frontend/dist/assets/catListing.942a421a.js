import{_ as r,r as a,o as _,a as p,c as d,b as n,d as u,L as g,p as v,e as h}from"./index.9fe0f635.js";const y=e=>(v("data-v-d578c780"),e=e(),h(),e),f={class:"app"},C={id:"allCategory"},w=y(()=>n("h1",{id:"categoryTitle"},"Category",-1)),L={setup(e){const s=a([]),i=async()=>{const t=await fetch("http://intproj21.sit.kmutt.ac.th:8080/at2/api/event");t.status===200?(s.value=await t.json(),console.log(s.value)):console.log("No Scheduled Events")},o=a([]),l=async()=>{const t=await fetch("http://intproj21.sit.kmutt.ac.th:8080/at2/api/eventCategory");t.status===200?(o.value=await t.json(),console.log(o.value)):console.log("No Category")};_(async()=>{await l(),await i()}),a("");const c=a({});return console.log(c),(t,m)=>(p(),d("div",f,[n("div",C,[w,u(g,{categories:o.value,onSelectCat:c.value},null,8,["categories","onSelectCat"])])]))}};var j=r(L,[["__scopeId","data-v-d578c780"]]);export{j as default};