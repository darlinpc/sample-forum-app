import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AticleComponent } from './article.component';

describe('PostComponent', () => {
  let component: AticleComponent;
  let fixture: ComponentFixture<AticleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AticleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
